package com.artal.eaccountant.auth;

public record LoginResponse(
        String message,
        UserResponse user
) {
}