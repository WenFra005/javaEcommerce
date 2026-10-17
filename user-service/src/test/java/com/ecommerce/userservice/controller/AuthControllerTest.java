package com.ecommerce.userservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ecommerce.userservice.config.SecurityConfig;
import com.ecommerce.userservice.exception.TokenRefreshException;
import com.ecommerce.userservice.model.RefreshToken;
import com.ecommerce.userservice.model.User;
import com.ecommerce.userservice.security.CustomAuthenticationEntryPoint;
import com.ecommerce.userservice.security.CustomUserDetails;
import com.ecommerce.userservice.security.JwtAuthFilter;
import com.ecommerce.userservice.security.JwtUtil;
import com.ecommerce.userservice.service.CustomUserDetailsService;
import com.ecommerce.userservice.service.RefreshTokenService;

import jakarta.servlet.http.HttpServletResponse;

@WebMvcTest(controllers = AuthController.class)
@Import({ SecurityConfig.class, JwtAuthFilter.class })
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private RefreshTokenService refreshTokenService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @BeforeEach
    void stubAuthenticationEntryPoint() throws Exception {
        doAnswer(invocation -> {
            HttpServletResponse response = invocation.getArgument(1);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }).when(customAuthenticationEntryPoint).commence(any(), any(), any());
    }

    @Test
    void testPostLogin_WithValidCredentials_ShouldReturnTokens() throws Exception {
        User mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setUserEmail("test@email.com");
        CustomUserDetails userDetails = new CustomUserDetails(mockUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateToken("test@email.com", 1L)).thenReturn("access-token");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token");
        when(refreshTokenService.createRefreshToken(1L)).thenReturn(refreshToken);

        String loginRequest = """
                {
                    "email": "test@email.com",
                    "password": "password"
                }
                """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"))
                .andExpect(jsonPath("$.userEmail").value("test@email.com"));

    }

    @Test
    void testPostLogin_WithInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        String loginRequest = """
                {
                    "email": "test@email.com",
                    "password": "wrongpassword"
                }
                """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    @Test
    void testPostLogout_WithValidToken_ShouldReturnNoContent() throws Exception {
        doNothing().when(refreshTokenService).revokeRefreshToken("valid-refresh-token");

        String logoutRequest = """
                {
                    "refreshToken": "valid-refresh-token"
                }
                """;

        mockMvc.perform(post("/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(logoutRequest))
                .andExpect(status().isNoContent());

    }

    @Test
    void testPostLogout_WithInvalidToken_ShouldReturnUnauthorized() throws Exception {
        doThrow(new TokenRefreshException("Refresh token invalid"))
                .when(refreshTokenService).revokeRefreshToken("invalid-refresh-token");

        String logoutRequest = """
                {
                    "refreshToken": "invalid-refresh-token"
                }
                """;

        mockMvc.perform(post("/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(logoutRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testPostRefresh_WithValidToken_ShouldReturnNewAccessToken() throws Exception {
        User mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setUserEmail("test@email.com");

        RefreshToken rotatedRefreshToken = new RefreshToken();
        rotatedRefreshToken.setToken("new-refresh-token");
        rotatedRefreshToken.setUser(mockUser);
        when(refreshTokenService.rotateRefreshToken("old-refresh-token")).thenReturn(rotatedRefreshToken);
        when(jwtUtil.generateToken("test@email.com", 1L)).thenReturn("new-access-token");

        String refreshRequest = """
                {
                    "refreshToken": "old-refresh-token"
                }
                """;

        mockMvc.perform(post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(refreshRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"));

    }

    @Test
    void testPostRefresh_WithInvalidToken_ShouldReturnForbidden() throws Exception {
        when(refreshTokenService.rotateRefreshToken("invalid-refresh-token"))
                .thenThrow(new TokenRefreshException(
                        "Refresh token inválido, expirado, revogado ou inexistente"));

        String refreshRequest = """
                {
                    "refreshToken": "invalid-refresh-token"
                }
                """;

        mockMvc.perform(post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(refreshRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value("Refresh token inválido, expirado, revogado ou inexistente"));
    }

}
