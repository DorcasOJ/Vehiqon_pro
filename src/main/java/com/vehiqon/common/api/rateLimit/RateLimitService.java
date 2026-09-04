package com.vehiqon.common.api.rateLimit;

import com.vehiqon.common.api.rateLimit.config.RateLimitProperties;
import com.vehiqon.common.exception.RateLimitException;
import com.vehiqon.common.exception.TooManyRequestException;
import com.vehiqon.features.infrastructure.redis.RedisCacheService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateLimitService {
    @Getter
    private final RateLimitProperties rateLimitProperties;
    private final RedisCacheService redisService;
    private final LettuceBasedProxyManager<String> proxyManager;

//    private final ConcurrentHashMap<String, Bucket> buckets= new ConcurrentHashMap<>();
//    private final Cache<String, Bucket> buckets = Caffeine.newBuilder()
//        .expireAfterAccess(Duration.ofHours(1))
//        .maximumSize(100_000)
//        .build();
    public Bucket resolveBucket (
            String key, RateLimitProperties.RateLimitConfig rateLimitConfig
    ) {
        return proxyManager.builder().build(key,() -> createBucket(rateLimitConfig));
    }

    private BucketConfiguration createBucket(RateLimitProperties.RateLimitConfig rateLimitConfig) {
        return BucketConfiguration.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(rateLimitConfig.getCapacity())
                        .refillIntervally(rateLimitConfig.getCapacity(), rateLimitConfig.getDuration())
                        .build()
                )
                .build();
    }

    public boolean tryConsume(String key, RateLimitProperties.RateLimitConfig config) {
        String redisKey = "RATE_LIMIT:" + key;
        Bucket bucket = resolveBucket(redisKey, config);
        return bucket.tryConsume(1);
    }

//    public void validateLogin(String email, String clientIp){
//        Bucket bucket = resolveBucket(
//                "RATE_LIMIT:LOGIN:" + email + ":" + clientIp,
//                rateLimitProperties.getLogin());
//        if (!bucket.tryConsume(1)) {
//            throw new RateLimitException("Too may login attempts. Please try again later");
//        }
//    }
//
//    public void validateRegister(String email, String clientIp){
//        Bucket bucket = resolveBucket(
//                "RATE_LIMIT:REGISTER:" + email + ":" + clientIp,
//                rateLimitProperties.getRegister());
//        if (!bucket.tryConsume(1)) {
//            throw new RateLimitException("Too may register attempts. Please try again later");
//        }
//    }
//
//    public void validateVerifyEmail(String email, String clientIp) {
//        Bucket bucket = resolveBucket(
//                "RATE_LIMIT:VERIFY_EMAIL:" + email + ":" + clientIp,
//                rateLimitProperties.getVerifyEmail());
//        if (!bucket.tryConsume(1)) {
//            throw new RateLimitException("Too may attempts to verify email. Please try again later");
//        }
//    }
//
//    public void validateResendVerificationEmail(String email, String clientIp) {
//        Bucket bucket = resolveBucket(
//                "RATE_LIMIT:RESEND_VERIFICATION_EMAIL:" + email + ":" + clientIp,
//                rateLimitProperties.getResendVerificationEmail());
//        if (!bucket.tryConsume(1)) {
//            throw new RateLimitException("Too may attempts requesting for verification email. Please try again later");
//        }
//    }
//
//    public void validateForgotPassword(String email, String clientIp) {
//        Bucket bucket = resolveBucket(
//                "RATE_LIMIT:FORGOT_PASSWORD:" + email + ":" + clientIp,
//                rateLimitProperties.getForgotPassword());
//        if (!bucket.tryConsume(1)) {
//            throw new RateLimitException("Too may attempts on forgot password. Please try again later");
//        }
//    }
//
//    public void validateResetPassword(String email, String clientIp) {
//        Bucket bucket = resolveBucket(
//                "RATE_LIMIT:RESET_PASSWORD:" + email + ":" + clientIp,
//                rateLimitProperties.getResetPassword());
//        if (!bucket.tryConsume(1)) {
//            throw new RateLimitException("Too may attempts on reset password. Please try again later");
//        }
//    }




}
