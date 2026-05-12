package com.artal.eaccountant.auth;

// Response sent when frontend checks the current user.
public record MeResponse(
        Long userId,
        String name,
        String email,
        String role
) {
}