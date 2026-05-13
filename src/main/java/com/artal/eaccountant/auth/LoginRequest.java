package com.artal.eaccountant.auth;

public record LoginRequest(
        String email,
        String password
) {
}