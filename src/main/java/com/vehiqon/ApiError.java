package com.vehiqon;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class ApiError {

    private int status;
    private String error;
    private String message;
    private Instant timestamp;
}