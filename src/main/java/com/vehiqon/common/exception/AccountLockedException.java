package com.vehiqon.common.exception;

import org.springframework.http.HttpStatus;

public class AccountLockedException extends BusinessException {
    public AccountLockedException(String message) {

        super(HttpStatus.UNAUTHORIZED, "ACCOUNT LOCKED", message);
    }
}
