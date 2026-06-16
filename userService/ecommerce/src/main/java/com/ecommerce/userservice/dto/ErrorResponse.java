package com.ecommerce.userservice.dto;

import java.time.LocalDateTime;

public class ErrorResponse {

    private int code;
    private String message;
    private String error;
    private LocalDateTime timestamp;
    private String path;
    
    public ErrorResponse() {
    }

    public ErrorResponse(int code, String message, String error, LocalDateTime timestamp, String path) {
        this.code = code;
        this.message = message;
        this.error = error;
        this.timestamp = timestamp;
        this.path = path;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    
}
