package com.vehiqon.common.exception;

import org.springframework.http.HttpStatus;

public class CredentialsExpiredException extends BusinessException {
    public CredentialsExpiredException(String message) {

        super(HttpStatus.UNAUTHORIZED, "EXPIRED CREDENTIALS", message);
    }
}
