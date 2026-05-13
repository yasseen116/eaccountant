package com.artal.eaccountant.auth;

public record UserResponse(
        Long id,
        String name,
        String email,
        String role,
        boolean active
) {
}