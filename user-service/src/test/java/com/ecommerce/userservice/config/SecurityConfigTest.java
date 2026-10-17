package com.ecommerce.userservice.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ecommerce.userservice.security.CustomAuthenticationEntryPoint;
import com.ecommerce.userservice.security.JwtAuthFilter;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Test
    void passwordEncoder_ShouldCreateBCryptEncoder() {
        SecurityConfig config = new SecurityConfig(jwtAuthFilter, authenticationEntryPoint);

        PasswordEncoder encoder = config.passwordEncoder();

        assertNotNull(encoder);
    }

    @Test
    void authenticationManager_ShouldDelegateToConfiguration() {
        SecurityConfig config = new SecurityConfig(jwtAuthFilter, authenticationEntryPoint);
        AuthenticationManager expectedManager = mock(AuthenticationManager.class);

        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(expectedManager);

        AuthenticationManager actualManager = config.authenticationManager(authenticationConfiguration);

        assertSame(expectedManager, actualManager);
    }
}
