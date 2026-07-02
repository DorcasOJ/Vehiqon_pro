package com.vehiqon.car_mgmt.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties (
        String secret,
        long expiration
) {
}
