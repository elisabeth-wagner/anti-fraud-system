package com.example.AntiFraudSystemApplication.controller;

import com.example.AntiFraudSystemApplication.model.SuspiciousIP;
import com.example.AntiFraudSystemApplication.model.StolenCard;
import com.example.AntiFraudSystemApplication.service.BlacklistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/antifraud")
public class BlacklistController {
    private final BlacklistService blacklistService;

    public BlacklistController(BlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    // IP-Endpunkte
    @PostMapping("/suspicious-ip")
    public SuspiciousIP addIp(@RequestBody Map<String, String> request) {
        return blacklistService.addIp(request.get("ip"));
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    public Map<String, String> deleteIp(@PathVariable String ip) {
        return blacklistService.deleteIp(ip);
    }

    @GetMapping("/suspicious-ip")
    public List<SuspiciousIP> listIps() {
        return blacklistService.getAllIps();
    }

    // Karten-Endpunkte
    @PostMapping("/stolencard")
    public StolenCard addCard(@RequestBody Map<String, String> request) {
        return blacklistService.addCard(request.get("number"));
    }

    @DeleteMapping("/stolencard/{number}")
    public Map<String, String> deleteCard(@PathVariable String number) {
        return blacklistService.deleteCard(number);
    }

    @GetMapping("/stolencard")
    public List<StolenCard> listCards() {
        return blacklistService.getAllCards();
    }
}