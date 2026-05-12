package com.artal.eaccountant.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /*
     * Temporary development security configuration.
     *
     * For now, we allow all API requests so React can call the backend
     * without login or JWT.
     *
     * Later, this will be replaced with real authentication.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        /*
         * Disable CSRF because this backend is currently used as a REST API.
         * React will send JSON requests to /api/**.
         */
        http.csrf(csrf -> csrf.disable());

        /*
         * Enable CORS support.
         * The allowed frontend URLs are configured in CorsConfig.
         */
        http.cors(Customizer.withDefaults());

        /*
         * Allow API, health check, and Swagger endpoints without login.
         * This is only for development.
         */
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/api/**",
                        "/actuator/health",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                ).permitAll()
                .anyRequest().permitAll()
        );

        /*
         * Build and return the final security filter chain.
         */
        return http.build();
    }
}