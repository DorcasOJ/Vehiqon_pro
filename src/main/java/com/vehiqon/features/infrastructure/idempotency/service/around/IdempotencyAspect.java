package com.vehiqon.features.infrastructure.idempotency.service.around;

import com.vehiqon.features.infrastructure.idempotency.dto.IdempotencyDto;
import com.vehiqon.features.infrastructure.idempotency.service.IdempotencyService;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class IdempotencyAspect {
    private final IdempotencyService idempotencyService;
    private final HttpServletRequest request;

    @Around("@annotation(idempotent")
    public Object handleIdempotency(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        String idempotencyKey = request.getHeader("Idempotency-Key");
        if (idempotencyKey.isBlank()) {
            return joinPoint.proceed();
        }
        IdempotencyDto cachedRecord = idempotencyService.checkOrLockKey(idempotencyKey, idempotent.expireInSeconds());
        if(cachedRecord != null) {
            return ResponseEntity.status(cachedRecord.statusCode()).body(cachedRecord.responseBody());
        }
        Object result = joinPoint.proceed();

        if(result instanceof ResponseEntity<?> responseEntity) {
            idempotencyService.saveResult(
                    idempotencyKey, responseEntity.getStatusCode().value(),
                    responseEntity.getBody(), idempotent.expireInSeconds()
            );
        } else {
            idempotencyService.saveResult(idempotencyKey, HttpStatus.OK.value(),
                    result, idempotent.expireInSeconds());
        }
        return result;
    }
}
