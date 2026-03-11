package com.example.AntiFraudSystemApplication.repository;

import com.example.AntiFraudSystemApplication.model.StolenCard;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StolenCardRepository extends JpaRepository<StolenCard, Long> {
    // Spring schreibt das SQL für die Suche nach der Kartennummer selbst
    Optional<StolenCard> findByNumber(String number);
}