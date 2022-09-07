package com.blogapp.backend.exception;

import com.blogapp.backend.payloads.ApiResponse;

import org.modelmapper.MappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApplicationExceptionHandler {

    private static final String ERROR_KEY = "message";

    Map<String, String> errorResponse = new HashMap<>();

    @ExceptionHandler(ResourceAlreadyExists.class)
    public ResponseEntity<ApiResponse> userAlreadyExistsException(ResourceAlreadyExists userAlreadyExists) {
        errorResponse.put(ERROR_KEY, userAlreadyExists.getMessage());
        return new ResponseEntity<>(new ApiResponse(errorResponse, false, HttpStatus.CONFLICT), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> userNotFoundException(ResourceNotFoundException userNotFound) {
        errorResponse.put(ERROR_KEY, userNotFound.getMessage());
        ApiResponse apiResponse = new ApiResponse(errorResponse, false, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentsNotFound.class)
    public ResponseEntity<ApiResponse> methodArgumentsNotFoundException(
            MethodArgumentsNotFound methodArgumentsNotFound) {
        errorResponse.put(ERROR_KEY, methodArgumentsNotFound.getMessage());
        ApiResponse apiResponse = new ApiResponse(errorResponse, false, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> methodArgumentNotValidException(
            MethodArgumentNotValidException methodArgumentNotValidException) {
        Map<String, String> resp = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            resp.put(fieldName, errorMessage);
        });
        ApiResponse apiResponse = new ApiResponse(resp, false, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> methodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
        errorResponse.put(ERROR_KEY, methodArgumentTypeMismatchException.getMessage());
        ApiResponse apiResponse = new ApiResponse(errorResponse, false, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MappingException.class)
    public ResponseEntity<ApiResponse> mappingException(MappingException mappingException) {
        errorResponse.put(ERROR_KEY, mappingException.getMessage());
        ApiResponse apiResponse = new ApiResponse(errorResponse, false, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> exception(Exception exception) {
        errorResponse.put(ERROR_KEY, exception.getMessage());
        ApiResponse apiResponse = new ApiResponse(errorResponse, false, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
