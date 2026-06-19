package com.ecommerce.userservice.Exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ecommerce.userservice.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message) {
        super(message);
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            getMessage(), 
            "User not found", 
            LocalDateTime.now(),
             request.getRequestURI()
            );
    
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
}
