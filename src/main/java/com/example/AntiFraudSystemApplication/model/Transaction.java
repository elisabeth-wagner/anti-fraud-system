package com.example.AntiFraudSystemApplication.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;
    private String ip;
    private String number;
    private String region;
    private LocalDateTime date;

    private String result; // ALLOWED, MANUAL_PROCESSING, PROHIBITED
    private String feedback = ""; // Wird in Phase 6 ausgefüllt
}