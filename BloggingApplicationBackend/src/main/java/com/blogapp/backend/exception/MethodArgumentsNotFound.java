package com.blogapp.backend.exception;

import java.io.Serial;

public class MethodArgumentsNotFound extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public MethodArgumentsNotFound(String message) {
        super(message);
    }

    public MethodArgumentsNotFound(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found in %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
