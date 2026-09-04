package com.vehiqon.features.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisCacheService {
    private final RedisTemplate<String, Object> redisTemplate;

//    Key-Value Operations with Expiry
    public void set(String key, Object value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? (T) value : null;
    }

    public Boolean setIfAbsent(String key, Object value, Duration timeout) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout);
    }

    public Object delete(String key) {
        return redisTemplate.delete(key);
    }

    public Object delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
    public Boolean expire(String key, Duration timeout) {
        return redisTemplate.expire(key, timeout);
    }

    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

//    Atomic Counter (Rate Limiting & Login Attempt Locking)
    public Long incrementAndExpireIfNew(String key, Duration expiryIfFirst) {
        Long count = redisTemplate.opsForValue().increment(key);
        if(count != null && count == 1) {
            redisTemplate.expire(key, expiryIfFirst);
        }
        return count;
    }

//    Pattern-based Deletion
    public void deleteByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if(keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
