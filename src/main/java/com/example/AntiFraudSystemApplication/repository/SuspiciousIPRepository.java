package com.example.AntiFraudSystemApplication.repository;

import com.example.AntiFraudSystemApplication.model.SuspiciousIP;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository bietet uns fertige Methoden: .save(), .findAll(), .delete()
public interface SuspiciousIPRepository extends JpaRepository<SuspiciousIP, Long> {
    // Spring schreibt das SQL für die Suche nach IP selbst
    Optional<SuspiciousIP> findByIp(String ip);
}
