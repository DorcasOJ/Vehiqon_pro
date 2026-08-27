package com.vehiqon.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BusinessException {
    public BadRequestException(String message) {

        super(HttpStatus.BAD_REQUEST, "INVALID_OPERATION", message);
    }
}
