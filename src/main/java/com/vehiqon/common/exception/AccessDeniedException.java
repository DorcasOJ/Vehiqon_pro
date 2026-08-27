package com.vehiqon.common.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends BusinessException {
    public AccessDeniedException(String message) {
        super(HttpStatus.FORBIDDEN, "ACCESS_DENIED", message);
    }

    public AccessDeniedException() {
        super(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "You do not have permission to access this resource");
    }
}
