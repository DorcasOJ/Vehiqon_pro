package com.vehiqon.common.exception;

public class TooManyRequestException extends BusinessException {
    public TooManyRequestException(String message) {
        super(message);
    }
}
