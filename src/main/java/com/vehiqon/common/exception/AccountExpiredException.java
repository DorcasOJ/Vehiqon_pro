package com.vehiqon.common.exception;

import org.springframework.http.HttpStatus;

public class AccountExpiredException extends BusinessException {
    public AccountExpiredException(String message) {
        super(HttpStatus.UNAUTHORIZED, "ACCOUNT EXPIRED", message);
    }
}
