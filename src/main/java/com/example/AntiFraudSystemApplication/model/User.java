package com.example.AntiFraudSystemApplication.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Benutzerentität für die Datenbank.
 * Beschreibt die Tabelle 'users', in der die Daten aller Systemteilnehmer gespeichert sind.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Eindeutige Benutzer-ID

    private String name; // Vollständiger Name

    @Column(unique = true)
    private String username; // Eindeutiger Login (E-Mail oder Benutzername)

    private String password; // Passwort in verschlüsselter Form

    /**
     * Benutzerrolle im System.
     * Nach Projektregeln:
     * - Der erste Benutzer wird ADMINISTRATOR.
     * - Die anderen werden MERCHANT.
     */
    private String role;
    private boolean accountNonLocked = true;
}