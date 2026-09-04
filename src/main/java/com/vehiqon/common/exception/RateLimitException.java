package com.vehiqon.common.exception;

import org.springframework.http.HttpStatus;

public class RateLimitException extends BusinessException {
    public RateLimitException(String message) {

        super(HttpStatus.TOO_MANY_REQUESTS, "TOO_MANY_REQUEST", message);
    }
}
