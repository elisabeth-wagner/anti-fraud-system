package com.example.AntiFraudSystemApplication.controller;

import com.example.AntiFraudSystemApplication.dto.UserRequest;
import com.example.AntiFraudSystemApplication.dto.UserResponse;
import com.example.AntiFraudSystemApplication.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody UserRequest request) {
        if (request.name() == null || request.username() == null || request.password() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return userService.register(request);
    }

    @GetMapping("/list")
    public List<UserResponse> listUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/user/{username}")
    public Map<String, String> deleteUser(@PathVariable String username) {
        return userService.deleteUser(username);
    }

    @PutMapping("/role")
    public UserResponse updateRole(@RequestBody Map<String, String> request) {
        return userService.updateRole(request);
    }

    @PutMapping("/access")
    public Map<String, String> updateAccess(@RequestBody Map<String, String> request) {
        return userService.updateAccess(request);
    }
}