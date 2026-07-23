package com.ecommerce.userservice.Security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecommerce.userservice.Service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class JwtAuthFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    private final String VALID_TOKEN = "valid-token";
    private final String EMAIL = "test@email.com";

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternal_WhenValidToken_ShouldAuthenticate() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);
        when(jwtUtil.extractEmail(VALID_TOKEN)).thenReturn(EMAIL);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        when(jwtUtil.validateToken(VALID_TOKEN, EMAIL)).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        assertEquals(userDetails, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        
        verify(filterChain).doFilter(request, response);

    }

    @Test
    void testDoFilterInternal_WhenNoToken_ShouldNotAuthenticate() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(filterChain).doFilter(request, response);
        verify(jwtUtil, never()).extractEmail(anyString());
    }

    @Test
    void testDoFilterInternal_WhenTokenIsExpired_ShouldNotAuthenticate() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);
        when(jwtUtil.extractEmail(VALID_TOKEN)).thenReturn(EMAIL);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        when(jwtUtil.validateToken(VALID_TOKEN, EMAIL)).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(filterChain).doFilter(request, response);
        verify(userDetailsService).loadUserByUsername(EMAIL);
    }

    @Test
    void testDoFilterInternal_WhenTokenIsInvalid_ShouldNotAuthenticate() throws Exception {
        String invalidToken = "invalid-token";
        
        when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidToken);
        when(jwtUtil.extractEmail(invalidToken)).thenThrow(new IllegalArgumentException("Token is invalid"));

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(filterChain).doFilter(request, response);
        verify(jwtUtil, never()).validateToken(anyString(), anyString());
    }

    @Test
    void testDoFilterInternal_WhenTokenIsInexisting_ShouldContinueFilterChain() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WhenGracefully_ShouldHandleException() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);
        when(jwtUtil.extractEmail(VALID_TOKEN)).thenThrow(new IllegalArgumentException("Unexpected error occurred"));

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(filterChain).doFilter(request, response);
        verify(jwtUtil, never()).validateToken(anyString(), anyString());
    }

}
