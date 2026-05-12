package com.artal.eaccountant.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    private final UserAccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Temporary in-memory token store for MVP development.
    private final Map<String, UserAccount> activeTokens = new HashMap<>();

    // Injects user repository and password encoder.
    public AuthService(UserAccountRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Validates email/password and returns login response.
    public LoginResponse login(LoginRequest request) {
        UserAccount user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!user.isActive()) {
            throw new RuntimeException("User is inactive");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = UUID.randomUUID().toString();
        activeTokens.put(token, user);

        return new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    // Removes token from active sessions.
    public void logout(String token) {
        activeTokens.remove(token);
    }

    // Returns current user from token.
    public MeResponse me(String token) {
        UserAccount user = activeTokens.get(token);

        if (user == null) {
            throw new RuntimeException("Not logged in");
        }

        return new MeResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}