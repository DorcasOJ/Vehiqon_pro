package com.vehiqon.common.exception;

import org.springframework.http.HttpStatus;

public class AccountDisabledException extends BusinessException {
    public AccountDisabledException(String message) {
        super(HttpStatus.UNAUTHORIZED, "UNNAUTHORISED", message);
    }
}
