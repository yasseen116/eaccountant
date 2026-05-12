package com.artal.eaccountant.auth;

import org.springframework.web.bind.annotation.*;

// Handles login, logout, and current user endpoints.
@RestController
public class AuthController {

    private final AuthService authService;

    // Injects auth service.
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Logs in user and returns token.
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    // Logs out user by removing token.
    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        authService.logout(token);
    }

    // Returns current logged-in user.
    @GetMapping("/me")
    public MeResponse me(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        return authService.me(token);
    }

    // Extracts token from Authorization header.
    private String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid authorization header");
        }

        return authorizationHeader.substring(7);
    }
}