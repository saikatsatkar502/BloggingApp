package com.blogapp.backend.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("user: %s is not authorized to %s with :  %s ", resourceName, fieldName, fieldValue));
    }
}
