package com.vehiqon.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:4200",
                "http://localhost:5173"
        ));

        corsConfiguration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        corsConfiguration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "X-Request-Id",
                "X-Device-Id",
                "Idempotency-Key",
                "Origin"
        ));

        corsConfiguration.setExposedHeaders(List.of(
                "Authorization",
                "X-Request-Id",
                "X-Device-Id",
                "Idempotency-Key"
        ));

//        Allow Credentials (Cookies, Authorization headers)
        corsConfiguration.setAllowCredentials(true);

//        Max Age (Caches pre-flight OPTIONS request results for 1 hour)
        corsConfiguration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
