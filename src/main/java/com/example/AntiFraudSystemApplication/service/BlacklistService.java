package com.example.AntiFraudSystemApplication.service;

import com.example.AntiFraudSystemApplication.model.SuspiciousIP;
import com.example.AntiFraudSystemApplication.model.StolenCard;
import com.example.AntiFraudSystemApplication.repository.SuspiciousIPRepository;
import com.example.AntiFraudSystemApplication.repository.StolenCardRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class BlacklistService {
    private final SuspiciousIPRepository ipRepository;
    private final StolenCardRepository cardRepository;

    public BlacklistService(SuspiciousIPRepository ipRepository, StolenCardRepository cardRepository) {
        this.ipRepository = ipRepository;
        this.cardRepository = cardRepository;
    }

    // --- Logik für IP ---
    public SuspiciousIP addIp(String ip) {
        if (!isValidIp(ip)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid IP format!");
        if (ipRepository.findByIp(ip).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "IP already exists!");

        SuspiciousIP suspiciousIP = new SuspiciousIP();
        suspiciousIP.setIp(ip);
        return ipRepository.save(suspiciousIP);
    }

    public Map<String, String> deleteIp(String ip) {
        if (!isValidIp(ip)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid IP format!");
        SuspiciousIP suspiciousIP = ipRepository.findByIp(ip)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "IP not found!"));
        ipRepository.delete(suspiciousIP);
        return Map.of("status", "IP " + ip + " successfully removed!");
    }

    public List<SuspiciousIP> getAllIps() {
        return ipRepository.findAll();
    }

    // --- Logik für Karten ---
    public StolenCard addCard(String number) {
        if (!isValidCard(number)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid card number!");
        if (cardRepository.findByNumber(number).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Card already exists!");

        StolenCard stolenCard = new StolenCard();
        stolenCard.setNumber(number);
        return cardRepository.save(stolenCard);
    }

    public Map<String, String> deleteCard(String number) {
        if (!isValidCard(number)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid card number!");
        StolenCard stolenCard = cardRepository.findByNumber(number)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found!"));
        cardRepository.delete(stolenCard);
        return Map.of("status", "Card " + number + " successfully removed!");
    }

    public List<StolenCard> getAllCards() {
        return cardRepository.findAll();
    }

    // Hilfsmethoden
    private boolean isValidIp(String ip) {
        String regex = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";
        return ip.matches(regex);
    }

    private boolean isValidCard(String number) {
        return number.length() == 16 && number.chars().allMatch(Character::isDigit);
    }
}