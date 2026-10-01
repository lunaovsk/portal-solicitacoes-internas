package com.portal_interno.api.infra.exception;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

public class CustomException extends RuntimeException {

    private final String errorCode;
    private final Map<String, Object> details;
    private final HttpStatus status;

    public CustomException(String message) {
        this(message, "ERROR_VALIDATION", null, HttpStatus.BAD_REQUEST);
    }

    public CustomException(String message, String errorCode) {
        this(message, errorCode, null, HttpStatus.BAD_REQUEST);
    }

    public CustomException(String message, String errorCode, Map<String, Object> details) {
        this(message, errorCode, details, HttpStatus.BAD_REQUEST);
    }

    private CustomException(String message, String errorCode, Map<String, Object> details, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.details = details != null ? Collections.unmodifiableMap(details) : null;
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public static CustomException userNotFound(String username) {
        return new CustomException(
                "User not found: " + username,
                "USER_NOT_FOUND",
                null,
                HttpStatus.NOT_FOUND
        );
    }
}
