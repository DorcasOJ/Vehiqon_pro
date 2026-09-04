package com.vehiqon;

import com.vehiqon.common.api.dto.RequestContext;
import com.vehiqon.common.api.dto.response.ApiError;
import com.vehiqon.common.api.dto.response.ErrorDetail;
import com.vehiqon.common.exception.BusinessException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@Hidden
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final RequestContext requestContext;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError<ErrorDetail>> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {


        ErrorDetail errorDetail = ErrorDetail.builder()
                .code(ex.getErrorCode())
                .message(ex.getMessage())
                .details(null)
                .build();

        ApiError<ErrorDetail> apiError = ApiError.<ErrorDetail>builder()
                .success(false)
                .responseCode(String.valueOf(ex.getStatus().value()))
                .message(ex.getMessage())
                .error(errorDetail)
                .path(request.getRequestURI())
                .requestId(requestContext.getRequestId())
                .build();

        return new ResponseEntity<>(apiError, ex.getStatus());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError<ErrorDetail>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.putIfAbsent(error.getField(), error.getDefaultMessage())
                );

        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("VALIDATION_FAILED")
                .message("Input validation failed")
                .details(errors)
                .build();

        ApiError<ErrorDetail> apiError = ApiError.<ErrorDetail>builder()
                .success(false)
                .responseCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .message("Invalid request payload")
                .error(errorDetail)
                .path(request.getRequestURI())
                .requestId(requestContext.getRequestId())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ApiError<ErrorDetail>> handleBusiness(
            DateTimeException ex,  HttpServletRequest request) {

        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("500")
                .message(ex.getMessage())
                .details(null)
                .build();

        ApiError<ErrorDetail> apiError = ApiError.<ErrorDetail>builder()
                .success(false)
                .responseCode("500")
                .message(ex.getMessage())
                .error(errorDetail)
                .path(request.getRequestURI())
                .requestId(requestContext.getRequestId())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError<ErrorDetail>> handleGlobalException(
            Exception ex, HttpServletRequest request
    ) {
        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred")
                .details(ex.getMessage())
                .build();
        ApiError<ErrorDetail> apiError = ApiError.<ErrorDetail>builder()
                .success(false)
                .responseCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .message("Internal Server error")
                .error(errorDetail)
                .path(request.getRequestURI())
                .requestId(requestContext.getRequestId())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }


//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ApiError> handleNotFound(
//            ResourceNotFoundException ex) {
//
//        ApiError error = new ApiError(
//                HttpStatus.NOT_FOUND.value(),
//                HttpStatus.NOT_FOUND.getReasonPhrase(),
//                ex.getMessage(),
//                Instant.now()
//        );
//
//        return ResponseEntity.documentStatus(HttpStatus.NOT_FOUND).body(error);
//    }

//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<ApiError> handleDataIntegrity(
//            DataIntegrityViolationException ex) {
//        log.error("Unhandled exception", ex);
//        ApiError error = new ApiError(
//                HttpStatus.CONFLICT.value(),
//                "Database Error",
//                "The operation violates a database constraint.",
//                Instant.now()
//        );
//
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
//    }
//
//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<ApiError> handleEntityNotFound(
//            EntityNotFoundException ex) {
//        log.error("Unhandled exception", ex);
//        ApiError error = new ApiError(
//                HttpStatus.NOT_FOUND.value(),
//                "Entity Not Found",
//                ex.getMessage(),
//                Instant.now()
//        );
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiError> handleException(
//            Exception ex) {
//        log.error("Unhandled exception", ex);
//
//        ApiError error = new ApiError(
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
//                "An unexpected error occurred.",
//                Instant.now()
//        );
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(error);
//    }
}