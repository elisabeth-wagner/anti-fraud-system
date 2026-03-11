package com.example.AntiFraudSystemApplication.repository;

import com.example.AntiFraudSystemApplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Benutzersuche nach Namen ohne Berücksichtigung der Groß-/Kleinschreibung
    Optional<User> findByUsernameIgnoreCase(String username);
}