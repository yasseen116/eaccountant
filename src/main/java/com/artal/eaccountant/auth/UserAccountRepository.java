package com.artal.eaccountant.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Handles database operations for users.
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    // Finds user by email for login.
    Optional<UserAccount> findByEmail(String email);
}