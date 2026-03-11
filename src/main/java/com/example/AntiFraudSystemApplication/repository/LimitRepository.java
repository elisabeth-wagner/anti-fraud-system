package com.example.AntiFraudSystemApplication.repository;

import com.example.AntiFraudSystemApplication.model.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {
    // Hier müssen keine Methoden geschrieben werden, Spring versteht selbst, wie man Limit nach ID abruft
}
