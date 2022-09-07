package com.blogapp.backend.payloads;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ApiResponse {
    private HttpStatus status;
    private Map<String, String> error;
    private Boolean success;

    // time stamp
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String timestamp;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

    public ApiResponse(Map<String, String> error, Boolean success, HttpStatus status) {
        this.status = status;
        this.error = error;
        this.success = success;
        this.timestamp = now.format(formatter);

    }

    public ApiResponse() {
        super();
    }

    public Map<String, String> getError() {
        return error;
    }

    public void setError(Map<String, String> error) {
        this.error = error;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
