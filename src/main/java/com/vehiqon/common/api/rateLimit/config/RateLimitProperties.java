package com.vehiqon.common.api.rateLimit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "rate-limit")
@Getter
@Setter
public class RateLimitProperties {
    private RateLimitConfig login = new RateLimitConfig();
    private RateLimitConfig register = new RateLimitConfig();
    private RateLimitConfig verifyEmail = new RateLimitConfig();
    private RateLimitConfig resendVerificationEmail = new RateLimitConfig();
    private RateLimitConfig forgotPassword = new RateLimitConfig();
    private RateLimitConfig resetPassword = new RateLimitConfig();
    private RateLimitConfig refreshToken = new RateLimitConfig();
    private RateLimitConfig authenticatedEndPoint = new RateLimitConfig();
    private RateLimitConfig unAuthenticatedEndPoint = new RateLimitConfig();

    @Getter
    @Setter
    public static class RateLimitConfig {

        private long capacity;
        private Duration duration;
    }
}
