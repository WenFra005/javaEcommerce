package com.ecommerce.userservice.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.ecommerce.userservice.security.JwtAuthFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Test
    void passwordEncoder_ShouldCreateBCryptEncoder() {
        SecurityConfig config = new SecurityConfig(jwtAuthFilter);

        PasswordEncoder encoder = config.passwordEncoder();

        assertNotNull(encoder);
    }

    @Test
    void authenticationManager_ShouldDelegateToConfiguration() throws Exception {
        SecurityConfig config = new SecurityConfig(jwtAuthFilter);
        AuthenticationManager expectedManager = mock(AuthenticationManager.class);

        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(expectedManager);

        AuthenticationManager actualManager = config.authenticationManager(authenticationConfiguration);

        assertSame(expectedManager, actualManager);
    }

    @Test
    void authenticationEntryPoint_ShouldWriteUnauthorizedResponse() throws Exception {
        SecurityConfig config = new SecurityConfig(jwtAuthFilter);

        AuthenticationEntryPoint entryPoint = config.authenticationEntryPoint();

        assertNotNull(entryPoint);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/private/resource");

        entryPoint.commence(request, response, new BadCredentialsException("invalid"));

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
    }
}
