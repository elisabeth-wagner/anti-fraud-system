package com.example.AntiFraudSystemApplication.dto;

// Objekt zum Senden der Antwort (ohne Passwort!)
public record UserResponse(Long id, String name, String username, String role) {}