package com.vehiqon.car_mgmt.common.exception;

public class ResourceAlreadyExistsException extends RuntimeException
{
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
