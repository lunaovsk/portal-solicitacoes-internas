package com.portal_interno.api.infra.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException exception) {
        ErrorResponse response = new ErrorResponse(
                exception.getMessage(),
                exception.getErrorCode(),
                exception.getDetails()
        );
        return ResponseEntity.status(exception.getStatus()).body(response);
    }

    public record ErrorResponse(String message, String errorCode, Object details) {
    }
}
