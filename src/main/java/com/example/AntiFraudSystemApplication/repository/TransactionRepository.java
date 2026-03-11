package com.example.AntiFraudSystemApplication.repository;

import com.example.AntiFraudSystemApplication.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Wir suchen alle Transaktionen für die Karte in der letzten Stunde
    List<Transaction> findAllByNumberAndDateBetween(String number, LocalDateTime start, LocalDateTime end);
}