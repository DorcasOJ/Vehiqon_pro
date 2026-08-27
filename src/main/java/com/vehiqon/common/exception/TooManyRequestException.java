package com.vehiqon.common.exception;

import org.springframework.http.HttpStatus;

public class TooManyRequestException extends BusinessException {
    public TooManyRequestException(String message) {

        super(HttpStatus.TOO_MANY_REQUESTS, "TOO_MANY)REQUEST", message);
    }
}
