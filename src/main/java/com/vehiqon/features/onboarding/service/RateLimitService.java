package com.vehiqon.features.onboarding.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {
//    private final ConcurrentHashMap<String, Bucket> buckets= new ConcurrentHashMap<>();
    private final Cache<String, Bucket> buckets = Caffeine.newBuilder()
        .expireAfterAccess(Duration.ofHours(1))
        .maximumSize(100_000)
        .build();
    public Bucket resolveBucket (
            String key, long capacity, Duration duration
    ) {
//        return buckets.computeIfAbsent(key, k -> Bucket.builder()
//                .addLimit(
//                        Bandwidth.builder()
//                                .capacity(capacity)
//                                .refillIntervally(capacity, duration)
//                                .build()
//                )
//                .build())
        return buckets.get(key, k -> creatBucket(capacity, duration));
    }

    private Bucket creatBucket(long capacity, Duration duration) {
        return Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(capacity)
                        .refillIntervally(capacity, duration)
                        .build()
                )
                .build();
    }

}
