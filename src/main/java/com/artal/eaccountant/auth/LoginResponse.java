package com.artal.eaccountant.auth;

// Response sent after successful login.
public record LoginResponse(
        String token,
        Long userId,
        String name,
        String email,
        String role
) {
}