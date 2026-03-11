package com.example.AntiFraudSystemApplication.controller;

import com.example.AntiFraudSystemApplication.model.Transaction;
import com.example.AntiFraudSystemApplication.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller zur Verarbeitung von Transaktionen und Feedback.
 */
@RestController
@RequestMapping("/api/antifraud")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Überprüfung der Transaktion (für HÄNDLER).
     */
    @PostMapping("/transaction")
    public Map<String, Object> calculateTransaction(@RequestBody Transaction transaction) {
        return transactionService.processTransaction(transaction);
    }

    /**
     * Empfang von Feedback (für SUPPORT).
     * Ermöglicht die Änderung von Limits basierend auf manueller Überprüfung.
     */
    @PutMapping("/feedback")
    public Transaction addFeedback(@RequestBody Map<String, String> request) {
        return transactionService.addFeedback(request);
    }
}