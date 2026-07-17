package com.vehiqon.features.onboarding.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.vehiqon.common.exception.TooManyRequestException;
import com.vehiqon.features.onboarding.config.RateLimitProperties;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RateLimitService {
    private final RateLimitProperties rateLimitProperties;

//    private final ConcurrentHashMap<String, Bucket> buckets= new ConcurrentHashMap<>();
    private final Cache<String, Bucket> buckets = Caffeine.newBuilder()
        .expireAfterAccess(Duration.ofHours(1))
        .maximumSize(100_000)
        .build();
    public Bucket resolveBucket (
            String key, RateLimitProperties.RateLimitConfig rateLimitConfig
    ) {
//        return buckets.computeIfAbsent(key, k -> Bucket.builder()
//                .addLimit(
//                        Bandwidth.builder()
//                                .capacity(capacity)
//                                .refillIntervally(capacity, duration)
//                                .build()
//                )
//                .build())
        return buckets.get(key, k -> creatBucket(rateLimitConfig));
    }

    private Bucket creatBucket( RateLimitProperties.RateLimitConfig rateLimitConfig) {
        return Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(rateLimitConfig.getCapacity())
                        .refillIntervally(rateLimitConfig.getCapacity(), rateLimitConfig.getDuration())
                        .build()
                )
                .build();
    }

    public void validateLogin(String email, String clientIp){
        Bucket bucket = resolveBucket(
                "LOGIN:" + email + ":" + clientIp,
                rateLimitProperties.getLogin());
        if (!bucket.tryConsume(1)) {
            throw new TooManyRequestException("Too may login attempts. Please try again later");
        }
    }


    public void validateRegister(String email, String clientIp){
        Bucket bucket = resolveBucket(
                "REGISTER:" + email + ":" + clientIp,
                rateLimitProperties.getRegister());
        if (!bucket.tryConsume(1)) {
            throw new TooManyRequestException("Too may register attempts. Please try again later");
        }
    }

    public void validateVerifyEmail(String email, String clientIp) {
        Bucket bucket = resolveBucket(
                "VERIFY_EMAIL:" + email + ":" + clientIp,
                rateLimitProperties.getVerifyEmail());
        if (!bucket.tryConsume(1)) {
            throw new TooManyRequestException("Too may attempts to verify email. Please try again later");
        }
    }

    public void validateResendVerificationEmail(String email, String clientIp) {
        Bucket bucket = resolveBucket(
                "RESEND_VERIFICATION_EMAIL:" + email + ":" + clientIp,
                rateLimitProperties.getResendVerificationEmail());
        if (!bucket.tryConsume(1)) {
            throw new TooManyRequestException("Too may attempts requesting for verification email. Please try again later");
        }
    }

    public void validateForgotPassword(String email, String clientIp) {
        Bucket bucket = resolveBucket(
                "FORGOT_PASSWORD:" + email + ":" + clientIp,
                rateLimitProperties.getForgotPassword());
        if (!bucket.tryConsume(1)) {
            throw new TooManyRequestException("Too may attempts on forgot password. Please try again later");
        }
    }

    public void validateResetPassword(String email, String clientIp) {
        Bucket bucket = resolveBucket(
                "RESET_PASSWORD:" + email + ":" + clientIp,
                rateLimitProperties.getResetPassword());
        if (!bucket.tryConsume(1)) {
            throw new TooManyRequestException("Too may attempts on reset password. Please try again later");
        }
    }



}
