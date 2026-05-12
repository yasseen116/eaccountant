package com.artal.eaccountant.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    /*
     * CORS configuration.
     *
     * This allows the React frontend to send requests to the Spring Boot backend.
     * Without this, the browser may block requests from localhost:5173 to localhost:8080.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {

        /*
         * Create a custom WebMvcConfigurer to define CORS rules.
         */
        return new WebMvcConfigurer() {

            /*
             * Apply CORS rules to all API endpoints.
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")

                        /*
                         * Allow React development servers.
                         */
                        .allowedOrigins(
                                "http://localhost:5173",
                                "http://localhost:5174",
                                "http://localhost:3000"
                        )
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}