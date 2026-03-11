package com.example.AntiFraudSystemApplication.service;

import com.example.AntiFraudSystemApplication.dto.UserRequest;
import com.example.AntiFraudSystemApplication.dto.UserResponse;
import com.example.AntiFraudSystemApplication.model.User;
import com.example.AntiFraudSystemApplication.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(UserRequest request) {
        if (userRepository.findByUsernameIgnoreCase(request.username()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Пользователь уже существует!");
        }

        User user = new User();
        user.setName(request.name());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));

        // Projektregel: Der Erste ist ADMIN (freigeschaltet), die anderen sind MERCHANT (gesperrt)
        if (userRepository.count() == 0) {
            user.setRole("ADMINISTRATOR");
            user.setAccountNonLocked(true);
        } else {
            user.setRole("MERCHANT");
            user.setAccountNonLocked(false);
        }

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getUsername(), savedUser.getRole());
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new UserResponse(u.getId(), u.getName(), u.getUsername(), u.getRole()))
                .collect(Collectors.toList());
    }

    public Map<String, String> deleteUser(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден!"));

        if (user.getRole().equals("ADMINISTRATOR")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нельзя удалить администратора!");
        }

        userRepository.delete(user);
        return Map.of("username", username, "status", "Deleted successfully!");
    }

    public UserResponse updateRole(Map<String, String> request) {
        String username = request.get("username");
        String role = request.get("role");

        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден!"));

        if (!role.equals("MERCHANT") && !role.equals("SUPPORT")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Недопустимая роль!");
        }

        if (user.getRole().equals("ADMINISTRATOR")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Роль администратора нельзя изменить!");
        }

        if (user.getRole().equals(role)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "У пользователя уже есть эта роль!");
        }

        user.setRole(role);
        userRepository.save(user);
        return new UserResponse(user.getId(), user.getName(), user.getUsername(), user.getRole());
    }

    public Map<String, String> updateAccess(Map<String, String> request) {
        String username = request.get("username");
        String operation = request.get("operation");

        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден!"));

        if (user.getRole().equals("ADMINISTRATOR")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нельзя заблокировать администратора!");
        }

        user.setAccountNonLocked(operation.equals("UNLOCK"));
        userRepository.save(user);

        String status = operation.equals("LOCK") ? "locked" : "unlocked";
        return Map.of("status", "User " + username + " " + status + "!");
    }
}