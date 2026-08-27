package com.vehiqon.common.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String resourceName, Object identifier) {
        super(HttpStatus.NOT_FOUND,  resourceName.toUpperCase() + "_NOT_FOUND",
                String.format("%s with identifier '%s' was not found", resourceName, identifier));
    }

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND" ,message);
    }
}
