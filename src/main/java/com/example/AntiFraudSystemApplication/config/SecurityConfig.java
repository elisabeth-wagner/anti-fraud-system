package com.example.AntiFraudSystemApplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(f -> f.disable()))
                .authorizeHttpRequests(auth -> auth
                        // Registrierung für alle verfügbar
                        .requestMatchers(HttpMethod.POST, "/api/auth/user").permitAll()

                        // Transaktionen — nur HÄNDLER
                        .requestMatchers("/api/antifraud/transaction/**").hasRole("MERCHANT")

                        // Feedback und schwarze Listen — nur SUPPORT
                        .requestMatchers(HttpMethod.PUT, "/api/antifraud/feedback").hasRole("SUPPORT")
                        .requestMatchers("/api/antifraud/suspicious-ip/**").hasRole("SUPPORT")
                        .requestMatchers("/api/antifraud/stolencard/**").hasRole("SUPPORT")

                        // Benutzerliste anzeigen — ADMIN und SUPPORT
                        .requestMatchers(HttpMethod.GET, "/api/auth/list").hasAnyRole("ADMINISTRATOR", "SUPPORT")

                        // Rollen- und Zugriffsverwaltung — nur ADMIN
                        .requestMatchers("/api/auth/**").hasRole("ADMINISTRATOR")

                        .anyRequest().authenticated()
                )
                .build();
    }
}