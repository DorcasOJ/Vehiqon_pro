package com.vehiqon.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidResourceException extends BusinessException {
    public InvalidResourceException(String message) {

        super(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", message);
    }
}
