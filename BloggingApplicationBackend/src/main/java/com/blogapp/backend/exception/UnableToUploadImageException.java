package com.blogapp.backend.exception;

public class UnableToUploadImageException extends RuntimeException {
    public UnableToUploadImageException(String message) {
        super(message);
    }

    public UnableToUploadImageException(String fileName, String title) {
        super(String.format("unable to upload image: %s with title: %s ", fileName, title));
    }
}
