package com.example.AntiFraudSystemApplication.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "limits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Limit {
    @Id
    private Long id = 1L; // Wir verwenden immer nur eine Zeile in der Datenbank mit ID = 1

    private double allowedLimit = 200;   // Standardmäßig 200
    private double manualLimit = 1500;  // Standardmäßig 1500
}