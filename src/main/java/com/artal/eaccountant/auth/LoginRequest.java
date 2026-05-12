package com.artal.eaccountant.auth;

// Request received from React login form.
public record LoginRequest(
        String email,
        String password
) {
}