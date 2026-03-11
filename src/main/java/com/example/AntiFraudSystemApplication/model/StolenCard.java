package com.example.AntiFraudSystemApplication.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stolen_cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StolenCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String number;
}