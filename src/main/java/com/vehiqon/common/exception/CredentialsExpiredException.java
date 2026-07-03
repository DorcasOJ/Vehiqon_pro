package com.vehiqon.common.exception;

public class CredentialsExpiredException extends BusinessException {
    public CredentialsExpiredException(String message) {
        super(message);
    }
}
