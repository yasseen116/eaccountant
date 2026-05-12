package com.artal.eaccountant.auth;

import jakarta.persistence.*;

@Entity
public class UserAccount {

    // Auto-generated primary key for each user.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User display name.
    private String name;

    // User email used for login.
    @Column(unique = true, nullable = false)
    private String email;

    // Hashed password, not plain text.
    private String password;

    // User role for MVP.
    private String role = "ADMIN";

    // Used to activate or deactivate users.
    private boolean active = true;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}