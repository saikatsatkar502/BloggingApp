package com.blogapp.backend.exception;

import java.io.Serial;

public class ResourceAlreadyExists extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceAlreadyExists(String message) {
        super(message);
    }

    public ResourceAlreadyExists(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
