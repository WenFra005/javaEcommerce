package com.ecommerce.userservice.exception;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.ecommerce.userservice.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

        private ErrorResponse buildErrorResponse(HttpStatus status, String message, String error,
                        HttpServletRequest request) {
                return new ErrorResponse(
                                status.value(),
                                message,
                                error,
                                Instant.now(),
                                request.getRequestURI());
        }

        @ExceptionHandler(EmailAlreadyExistsException.class)
        public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception,
                        HttpServletRequest request) {
                ErrorResponse errorResponse = buildErrorResponse(
                                HttpStatus.CONFLICT,
                                exception.getMessage(),
                                "Email already exists",
                                request);

                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
                        MethodArgumentNotValidException exception, HttpServletRequest request) {
                String errorMessage = exception.getBindingResult().getFieldErrors().stream()
                                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                .collect(Collectors.joining(", "));

                ErrorResponse errorResponse = buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                errorMessage,
                                "Validation error",
                                request);

                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ErrorResponse> handleDataIntegrityException(DataIntegrityViolationException exception,
                        HttpServletRequest request) {
                String errorMessage = "Data integrity violation: " + exception.getMostSpecificCause().getMessage();

                ErrorResponse errorResponse = buildErrorResponse(
                                HttpStatus.CONFLICT,
                                errorMessage,
                                "Data integrity violation",
                                request);

                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException exception,
                        HttpServletRequest request) {
                ErrorResponse errorResponse = buildErrorResponse(
                                HttpStatus.UNAUTHORIZED,
                                exception.getMessage(),
                                "Unauthorized",
                                request);
                return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(TokenRefreshException.class)
        public ResponseEntity<ErrorResponse> handleTokenRefreshException(TokenRefreshException exception,
                        HttpServletRequest request) {
                ErrorResponse errorResponse = buildErrorResponse(
                                HttpStatus.UNAUTHORIZED,
                                exception.getMessage(),
                                "Refresh token error",
                                request);
                return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);

        }

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception,
                        HttpServletRequest request) {

                ErrorResponse errorResponse = buildErrorResponse(
                                HttpStatus.NOT_FOUND,
                                exception.getMessage(),
                                "User not found",
                                request);

                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
                        HttpMessageNotReadableException exception, HttpServletRequest request) {
                Throwable rootCause = exception.getMostSpecificCause();

                String message = rootCause.getMessage();

                if (message != null && message.startsWith("problem: ")) {
                        message = message.substring(9);
                }

                ErrorResponse errorResponse = buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                message,
                                "Validation Error",
                                request);

                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(ValidationException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(ValidationException exception,
                        HttpServletRequest request) {
                ErrorResponse errorResponse = buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                exception.getMessage(),
                                "Validation failed",
                                request);

                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErrorResponse> handleAcessDeniedException(AccessDeniedException exception,
                        HttpServletRequest request) {
                ErrorResponse errorResponse = buildErrorResponse(
                                HttpStatus.FORBIDDEN,
                                exception.getMessage() != null ? exception.getMessage() : "Access denied",
                                "Forbidden",
                                request);

                return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(Exception exception, HttpServletRequest request) {
                ErrorResponse errorResponse = buildErrorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                exception.getMessage(),
                                "Internal server error",
                                request);

                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

}
