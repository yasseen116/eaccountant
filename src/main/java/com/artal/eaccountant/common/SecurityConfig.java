package com.artal.eaccountant.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Temporary development security configuration.
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        http.cors(Customizer.withDefaults());

        // Disable Spring Security's default /logout so our controller handles it.
        http.logout(logout -> logout.disable());

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/login",
                        "/logout",
                        "/me",
                        "/api/**",
                        "/actuator/health",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                ).permitAll()
                .anyRequest().permitAll()
        );

        return http.build();
    }

    // Hashes and checks passwords safely.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}