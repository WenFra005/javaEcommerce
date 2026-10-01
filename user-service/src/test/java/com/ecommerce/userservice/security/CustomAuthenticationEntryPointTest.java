package com.ecommerce.userservice.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class CustomAuthenticationEntryPointTest {

    @Test
    void commence_ShouldWriteUnauthorizedJsonResponse() throws Exception {
        CustomAuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);

        when(request.getRequestURI()).thenReturn("/protected/resource");
        when(response.getWriter()).thenReturn(writer);

        entryPoint.commence(request, response, new BadCredentialsException("Credenciais inválidas"));

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");

        writer.flush();
        String body = out.toString();

        assertNotNull(body);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.value());
        assertEquals(true, body.contains("Credenciais inválidas"));
        assertEquals(true, body.contains("Unauthorized"));
        assertEquals(true, body.contains("/protected/resource"));
    }
}
