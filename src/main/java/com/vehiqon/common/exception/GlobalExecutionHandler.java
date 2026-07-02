package com.vehiqon.common.exception;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.utils.AccountUtils;
import org.springframework.http.HttpStatus;
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
                        errors.putIfAbsent(error.getField(), error.getDefaultMessage())
                        );
        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .responseCode(AccountUtils.VALIDATION_ERROR_CODE)
                .responseMessage(AccountUtils.VALIDATION_ERROR_MESSAGE)
                .data(errors)
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceAlreadyExists(
            ResourceAlreadyExistsException ex
    ) {

        ApiResponse<Object> response = ApiResponse.builder()
                .responseCode(AccountUtils.USER_EXIST_CODE)
                .responseMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidCredentials(
            InvalidCredentialsException ex) {

        ApiResponse<Object> response = ApiResponse.builder()
                .responseCode(AccountUtils.INVALID_CREDENTIALS_CODE)
                .responseMessage(ex.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        ApiResponse<Object> response = ApiResponse.builder()
                .responseCode("500")
                .responseMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

//    @ExceptionHandler(ResourceNotFoundException.class)
//...
//
//    @ExceptionHandler(InvalidCredentialsException.class)
//...
//
//    @ExceptionHandler(AccessDeniedException.class)
//...
//
//    @ExceptionHandler(Exception.class)
//...
}
