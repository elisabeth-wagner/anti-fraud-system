package com.example.AntiFraudSystemApplication.service;

import com.example.AntiFraudSystemApplication.model.*;
import com.example.AntiFraudSystemApplication.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final SuspiciousIPRepository ipRepository;
    private final StolenCardRepository cardRepository;
    private final LimitRepository limitRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              SuspiciousIPRepository ipRepository,
                              StolenCardRepository cardRepository,
                              LimitRepository limitRepository) {
        this.transactionRepository = transactionRepository;
        this.ipRepository = ipRepository;
        this.cardRepository = cardRepository;
        this.limitRepository = limitRepository;
    }

    private Limit getCurrentLimits() {
        return limitRepository.findById(1L).orElseGet(() -> {
            Limit initial = new Limit();
            return limitRepository.save(initial);
        });
    }

    public Map<String, Object> processTransaction(Transaction request) {
        Limit limits = getCurrentLimits();
        List<String> info = new ArrayList<>();
        String result = "ALLOWED";

        // 1. Überprüfung der schwarzen Listen
        boolean ipBlacklisted = ipRepository.findByIp(request.getIp()).isPresent();
        boolean cardBlacklisted = cardRepository.findByNumber(request.getNumber()).isPresent();
        if (cardBlacklisted) info.add("card-number");
        if (ipBlacklisted) info.add("ip");

        // 2. Überprüfung der Korrelationen (letzte Stunde)
        LocalDateTime hourAgo = request.getDate().minusHours(1);
        List<Transaction> history = transactionRepository.findAllByNumberAndDateBetween(
                request.getNumber(), hourAgo, request.getDate());

        long distinctIPs = history.stream().map(Transaction::getIp).filter(ip -> !ip.equals(request.getIp())).distinct().count();
        long distinctRegions = history.stream().map(Transaction::getRegion).filter(reg -> !reg.equals(request.getRegion())).distinct().count();

        if (distinctIPs > 2) info.add("ip-correlation");
        else if (distinctIPs == 2) info.add("ip-correlation");

        if (distinctRegions > 2) info.add("region-correlation");
        else if (distinctRegions == 2) info.add("region-correlation");

        // 3. Überprüfung des Betrags anhand dynamischer Limits
        if (request.getAmount() > limits.getManualLimit()) info.add("amount");
        else if (request.getAmount() > limits.getAllowedLimit()) info.add("amount");

        // Entscheidungslogik
        Collections.sort(info);
        if (cardBlacklisted || ipBlacklisted || distinctIPs > 2 || distinctRegions > 2 || request.getAmount() > limits.getManualLimit()) {
            result = "PROHIBITED";
        } else if (distinctIPs == 2 || distinctRegions == 2 || request.getAmount() > limits.getAllowedLimit()) {
            result = "MANUAL_PROCESSING";
        }

        if (info.isEmpty()) info.add("none");

        request.setResult(result);
        // SPEICHERN UND ABRUFEN DES OBJEKTS MIT ID
        Transaction savedTransaction = transactionRepository.save(request);

        // RÜCKGABE VON ID UND ANDEREN FELDERN
        return Map.of(
                "id", savedTransaction.getId(),
                "result", result,
                "info", String.join(", ", info.stream().distinct().collect(Collectors.toList()))
        );
    }

    public Transaction addFeedback(Map<String, String> request) {
        Long transactionId = Long.parseLong(String.valueOf(request.get("transactionId")));
        String feedback = request.get("feedback");

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Транзакция не найдена"));

        if (transaction.getFeedback() != null && !transaction.getFeedback().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Отзыв уже оставлен");
        }

        if (transaction.getResult().equals(feedback)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Вердикт совпадает с отзывом");
        }

        updateLimitsLogic(transaction.getResult(), feedback, transaction.getAmount());

        transaction.setFeedback(feedback);
        return transactionRepository.save(transaction);
    }

    private void updateLimitsLogic(String systemResult, String feedback, Long amount) {
        Limit limits = getCurrentLimits();

        if (systemResult.equals("ALLOWED") && feedback.equals("MANUAL_PROCESSING")) {
            limits.setAllowedLimit(Math.ceil(0.8 * limits.getAllowedLimit() - 0.2 * amount));
        } else if (systemResult.equals("ALLOWED") && feedback.equals("PROHIBITED")) {
            limits.setAllowedLimit(Math.ceil(0.8 * limits.getAllowedLimit() - 0.2 * amount));
            limits.setManualLimit(Math.ceil(0.8 * limits.getManualLimit() - 0.2 * amount));
        } else if (systemResult.equals("MANUAL_PROCESSING") && feedback.equals("ALLOWED")) {
            limits.setAllowedLimit(Math.ceil(0.8 * limits.getAllowedLimit() + 0.2 * amount));
        } else if (systemResult.equals("MANUAL_PROCESSING") && feedback.equals("PROHIBITED")) {
            limits.setManualLimit(Math.ceil(0.8 * limits.getManualLimit() - 0.2 * amount));
        } else if (systemResult.equals("PROHIBITED") && feedback.equals("ALLOWED")) {
            limits.setAllowedLimit(Math.ceil(0.8 * limits.getAllowedLimit() + 0.2 * amount));
            limits.setManualLimit(Math.ceil(0.8 * limits.getManualLimit() + 0.2 * amount));
        } else if (systemResult.equals("PROHIBITED") && feedback.equals("MANUAL_PROCESSING")) {
            limits.setManualLimit(Math.ceil(0.8 * limits.getManualLimit() + 0.2 * amount));
        }

        limitRepository.save(limits);
    }
}