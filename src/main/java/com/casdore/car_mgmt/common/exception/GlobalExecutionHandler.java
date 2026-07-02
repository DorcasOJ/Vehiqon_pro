package com.casdore.car_mgmt.common.exception;

import com.casdore.car_mgmt.common.dto.response.ApiResponse;
import com.casdore.car_mgmt.common.utils.AccountUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExecutionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                        );
        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .responseCode(AccountUtils.VALIDATION_ERROR_CODE)
                .responseMessage(AccountUtils.VALIDATION_ERROR_MESSAGE)
                .data(errors)
                .build();
        return ResponseEntity.badRequest().body(response);
    }
}
