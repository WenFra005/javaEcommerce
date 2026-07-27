package com.ecommerce.userservice.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.ecommerce.userservice.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test/uri");
    }

    @Test
    void testHandleAcessDeniedException() {
        AccessDeniedException exception = new AccessDeniedException("Acesso negado");

        ResponseEntity<ErrorResponse> responseEntity = handler.handleAcessDeniedException(exception, request);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        ErrorResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(403, body.getCode());
        assertEquals("Acesso negado", body.getMessage());
        assertEquals("Forbidden", body.getError());
        assertEquals("/test/uri", body.getPath());
        assertNotNull(body.getTimestamp());

    }

    @Test
    void testHandleAuthenticationException() {
        AuthenticationException exception = mock(AuthenticationException.class);
        when(exception.getMessage()).thenReturn("Credenciais inválidas ou token expirado");

        ResponseEntity<ErrorResponse> responseEntity = handler.handleAuthenticationException(exception, request);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        ErrorResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(401, body.getCode());
        assertEquals("Credenciais inválidas ou token expirado", body.getMessage());
        assertEquals("Unauthorized", body.getError());
        assertEquals("/test/uri", body.getPath());
        assertNotNull(body.getTimestamp());

    }

    @Test
    void testHandleDataIntegrityException() {
        DataIntegrityViolationException exception = new DataIntegrityViolationException(
                "Violação de integridade de dados");

        ResponseEntity<ErrorResponse> responseEntity = handler.handleDataIntegrityException(exception, request);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        ErrorResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(409, body.getCode());
        assertEquals("Data integrity violation: Violação de integridade de dados", body.getMessage());
        assertEquals("Data integrity violation", body.getError());
        assertEquals("/test/uri", body.getPath());
        assertNotNull(body.getTimestamp());

    }

    @Test
    void testHandleEmailAlreadyExistsException() {
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException("Email já existe");

        ResponseEntity<ErrorResponse> responseEntity = handler.handleEmailAlreadyExistsException(exception, request);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        ErrorResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(409, body.getCode());
        assertEquals("Email já existe", body.getMessage());
        assertEquals("Email already exists", body.getError());
        assertEquals("/test/uri", body.getPath());
        assertNotNull(body.getTimestamp());

    }

    @Test
    void testHandleGenericException() {
        Exception exception = new Exception("Erro genérico");

        ResponseEntity<ErrorResponse> responseEntity = handler.handleGenericException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        ErrorResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(500, body.getCode());
        assertEquals("Erro genérico", body.getMessage());
        assertEquals("Internal server error", body.getError());
        assertEquals("/test/uri", body.getPath());
        assertNotNull(body.getTimestamp());

    }

    @Test
    void testHandleHttpMessageNotReadableException() {
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);

        Throwable rootCause = new Throwable("Formato de data inválido");
        doReturn(rootCause).when(exception).getMostSpecificCause();
        ResponseEntity<ErrorResponse> responseEntity = handler.handleHttpMessageNotReadableException(exception,
                request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(400, body.getCode());
        assertEquals("Formato de data inválido", body.getMessage());
        assertEquals("Validation Error", body.getError());
        assertEquals("/test/uri", body.getPath());
        assertNotNull(body.getTimestamp());

    }

    @Test
    void testHandleTokenRefreshException() {
        TokenRefreshException exception = new TokenRefreshException("Token expirado");

        ResponseEntity<ErrorResponse> responseEntity = handler.handleTokenRefreshException(exception, request);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        ErrorResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(401, body.getCode());
        assertEquals("Token expirado", body.getMessage());
        assertEquals("Refresh token error", body.getError());
        assertEquals("/test/uri", body.getPath());
        assertNotNull(body.getTimestamp());

    }

    @Test
    void testHandleUserNotFoundException() {
        UserNotFoundException exception = new UserNotFoundException("Usuário não encontrado");

        ResponseEntity<ErrorResponse> responseEntity = handler.handleUserNotFoundException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ErrorResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(404, body.getCode());
        assertEquals("Usuário não encontrado", body.getMessage());
        assertEquals("User not found", body.getError());
        assertEquals("/test/uri", body.getPath());
        assertNotNull(body.getTimestamp());

    }

    @Test
    void testHandleValidationException() {
        ValidationException exception = new ValidationException("Campo inválido");

        ResponseEntity<ErrorResponse> responseEntity = handler.handleValidationException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(400, body.getCode());
        assertEquals("Campo inválido", body.getMessage());
        assertEquals("Validation failed", body.getError());
        assertEquals("/test/uri", body.getPath());
        assertNotNull(body.getTimestamp());

    }

    @Test
    void testHandleMethodArgumentNotValidException() {
        FieldError fieldError = mock(FieldError.class);
        when(fieldError.getField()).thenReturn("cpf");
        when(fieldError.getDefaultMessage()).thenReturn("CPF inválido");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ErrorResponse> responseEntity = handler.handleMethodArgumentNotValidException(exception,
                request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(400, body.getCode());
        assertEquals("cpf: CPF inválido", body.getMessage());
        assertEquals("Validation error", body.getError());
        assertEquals("/test/uri", body.getPath());
        assertNotNull(body.getTimestamp());

    }
}
