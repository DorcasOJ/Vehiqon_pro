package com.vehiqon.common.exception;

import org.springframework.http.HttpStatus;

public class IdempotencyException extends BusinessException {
    public IdempotencyException(String message) {

        super(HttpStatus.BAD_GATEWAY, "IDEMPOTENCY CONFLICT", message);
    }
}
