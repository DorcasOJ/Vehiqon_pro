package com.vehiqon.common.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotCreatedException extends BusinessException {
    public ResourceNotCreatedException(String message) {

        super(HttpStatus.BAD_REQUEST, "RESOURCE_NOT_CREATED", message);
    }
}
