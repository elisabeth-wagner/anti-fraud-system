package com.example.AntiFraudSystemApplication.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "suspicious_ips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuspiciousIP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ip;
}