package com.vehiqon.common.exception;

public class AccountLockedException extends BusinessException {
    public AccountLockedException(String message) {
        super(message);
    }
}
