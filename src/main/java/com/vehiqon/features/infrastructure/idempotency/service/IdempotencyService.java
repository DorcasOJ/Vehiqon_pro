package com.vehiqon.features.infrastructure.idempotency.service;

import com.vehiqon.common.exception.BusinessException;
import com.vehiqon.features.infrastructure.idempotency.dto.IdempotencyDto;
import com.vehiqon.features.infrastructure.redis.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class IdempotencyService {
    private final RedisCacheService redisCacheService;
    private static final String IDEMPOTENCY_KEY_PREFIX = "idempotency:";

    public IdempotencyDto checkOrLockKey(String idempotencyKey, int expiryInSeconds) {
        String key = IDEMPOTENCY_KEY_PREFIX+ idempotencyKey;
        Object existing = redisCacheService.get(key);
        if(existing instanceof IdempotencyDto record) {
            if ("PROCESSING".equals(record.status())) {
                throw new BusinessException(HttpStatus.CONFLICT, "CONCURRENCY_REQUEST",
                        "A request with this Idempotency-Key is currently being processed.");
            }
            return record;
        }

        redisCacheService.set(key, IdempotencyDto.processing(), Duration.ofSeconds(expiryInSeconds));
        return null;
    }

    public void saveResult(String idempotencyKey, int statusCode, Object
                            body, int expireInSeconds) {
        String key = IDEMPOTENCY_KEY_PREFIX+ idempotencyKey;
        IdempotencyDto completedDto = IdempotencyDto.completed(statusCode, body);
        redisCacheService.set(key, completedDto, Duration.ofSeconds(expireInSeconds));
    }
}
