package com.vehiqon.common.exception;

public class AccountExpiredException extends BusinessException {
    public AccountExpiredException(String message) {
        super(message);
    }
}
