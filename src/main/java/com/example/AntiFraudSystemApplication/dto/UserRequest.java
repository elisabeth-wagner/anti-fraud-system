package com.example.AntiFraudSystemApplication.dto;

// Objekt zum Empfangen von Daten von Postman
public record UserRequest(String name, String username, String password) {}