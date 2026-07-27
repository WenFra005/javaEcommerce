package com.ecommerce.userservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.ecommerce.userservice.exception.TokenRefreshException;
import com.ecommerce.userservice.exception.UserNotFoundException;
import com.ecommerce.userservice.model.RefreshToken;
import com.ecommerce.userservice.model.User;
import com.ecommerce.userservice.repository.RefreshTokenRepository;
import com.ecommerce.userservice.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private User mockUser;
    private RefreshToken mockRefreshToken;
    private final Long USER_ID = 1L;
    private final String TOKEN_VALUE = "valid-token";

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setUserId(USER_ID);

        mockRefreshToken = new RefreshToken();
        mockRefreshToken.setToken(TOKEN_VALUE);
        mockRefreshToken.setUser(mockUser);
        mockRefreshToken.setRevoked(false);
        mockRefreshToken.setExpiryDate(Instant.now().plusSeconds(3600));

        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenDurationMs", 3600000L);

    }

    @Test
    void testCreateRefreshToken_WhenUserExists_ShouldCreateNewToken() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(mockUser));
        when(refreshTokenRepository.findByUser(mockUser)).thenReturn(Optional.of(mockRefreshToken));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken createdToken = refreshTokenService.createRefreshToken(USER_ID);

        assertNotNull(createdToken);
        assertNotNull(createdToken.getToken());
        assertFalse(createdToken.isRevoked());
        assertTrue(createdToken.getExpiryDate().isAfter(Instant.now()));
        assertEquals(mockUser, createdToken.getUser());
        verify(refreshTokenRepository).save(any(RefreshToken.class));

    }

    @Test
    void testCreateRefreshToken_WhenUserNotFound_ShouldThrowUserNotFoundException() {
        Long invalidUserId = 999L;
        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> refreshTokenService.createRefreshToken(invalidUserId));
        verify(refreshTokenRepository, never()).save(any());

    }

    @Test
    void testGetUserFromRefreshToken_WhenTokenExists_ShouldReturnUser() {
        when(refreshTokenRepository.findByToken(TOKEN_VALUE)).thenReturn(Optional.of(mockRefreshToken));

        User user = refreshTokenService.getUserFromRefreshToken(TOKEN_VALUE);

        assertEquals(mockUser, user);

    }

    @Test
    void testGetUserFromRefreshToken_WhenTokenNotFound_ShouldThrowRefreshTokenException() {
        String invalidToken = "invalid-token";

        when(refreshTokenRepository.findByToken(invalidToken)).thenReturn(Optional.empty());

        assertThrows(TokenRefreshException.class,
                () -> refreshTokenService.getUserFromRefreshToken(invalidToken));

    }

    @Test
    void testRevokeAllRefreshTokensForUser_WhenUserExists_ShouldRevokeAllTokens() {

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(mockUser));

        refreshTokenService.revokeAllRefreshTokensForUser(USER_ID);
        verify(refreshTokenRepository).deleteByUser(mockUser);

    }

    @Test
    void testRevokeAllRefreshTokensForUser_WhenUserNotFound_ShouldThrowUserNotFoundException() {
        Long invalidUserId = 999L;
        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> refreshTokenService.revokeAllRefreshTokensForUser(invalidUserId));
        verify(refreshTokenRepository, never()).deleteByUser(any());
    }

    @Test
    void testRevokeRefreshToken_WhenTokenExists_ShouldRevokeToken() {
        when(refreshTokenRepository.findByToken(TOKEN_VALUE)).thenReturn(Optional.of(mockRefreshToken));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(mockRefreshToken);

        refreshTokenService.revokeRefreshToken(TOKEN_VALUE);

        assertTrue(mockRefreshToken.isRevoked());
        verify(refreshTokenRepository).save(mockRefreshToken);

    }

    @Test
    void testRevokeRefreshToken_WhenTokenNotFound_ShouldThrowRefreshTokenException() {
        String invalidToken = "invalid-token";

        when(refreshTokenRepository.findByToken(invalidToken)).thenReturn(Optional.empty());

        assertThrows(TokenRefreshException.class,
                () -> refreshTokenService.revokeRefreshToken(invalidToken));
    }

    @Test
    void testValidateRefreshToken_WhenTokenIsValid_ShouldReturnToken() {
        when(refreshTokenRepository.findByToken(TOKEN_VALUE)).thenReturn(Optional.of(mockRefreshToken));

        RefreshToken validToken = refreshTokenService.validateRefreshToken(TOKEN_VALUE);

        assertEquals(mockRefreshToken, validToken);

    }

    @Test
    void testValidadeRefreshToken_WhenTokenIsRevoked_ShouldThrowRefreshTokenException() {
        mockRefreshToken.setRevoked(true);

        when(refreshTokenRepository.findByToken(TOKEN_VALUE)).thenReturn(Optional.of(mockRefreshToken));

        assertThrows(TokenRefreshException.class,
                () -> refreshTokenService.validateRefreshToken(TOKEN_VALUE));
    }

    @Test
    void testValidateRefreshToken_WhenTokenIsExpired_ShouldThrowRefreshTokenException() {
        mockRefreshToken.setExpiryDate(Instant.now().minusSeconds(1));

        when(refreshTokenRepository.findByToken(TOKEN_VALUE)).thenReturn(Optional.of(mockRefreshToken));

        assertThrows(TokenRefreshException.class,
                () -> refreshTokenService.validateRefreshToken(TOKEN_VALUE));
    }

    @Test
    void testValidateRefreshToken_WhenTokenNotFound_ShouldThrowRefreshTokenException() {
        String invalidToken = "invalid-token";

        when(refreshTokenRepository.findByToken(invalidToken)).thenReturn(Optional.empty());

        assertThrows(TokenRefreshException.class,
                () -> refreshTokenService.validateRefreshToken(invalidToken));
    }
}
