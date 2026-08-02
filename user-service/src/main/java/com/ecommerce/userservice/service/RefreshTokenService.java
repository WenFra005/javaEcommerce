package com.ecommerce.userservice.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.userservice.exception.TokenRefreshException;
import com.ecommerce.userservice.exception.UserNotFoundException;
import com.ecommerce.userservice.model.RefreshToken;
import com.ecommerce.userservice.model.User;
import com.ecommerce.userservice.repository.RefreshTokenRepository;
import com.ecommerce.userservice.repository.UserRepository;

@Service
public class RefreshTokenService {

    private static final String USER_NOT_FOUND_MESSAGE = "User not found with id: ";
    private static final String REFRESH_TOKEN_NOT_FOUND_MESSAGE = "Refresh token not found: ";

    @Value("${jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;

    public RefreshTokenService() {
    }

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        User user = findUserById(userId);

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElse(new RefreshToken());

        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = findRefreshTokenByToken(token);

        if (refreshToken.isRevoked()) {
            throw new TokenRefreshException("Refresh token has been revoked: " + token);
        }

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new TokenRefreshException("Refresh token has expired: " + token);
        }

        return refreshToken;
    }

    @Transactional
    public void revokeRefreshToken(String token) {
        RefreshToken refreshToken = findRefreshTokenByToken(token);
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void revokeAllRefreshTokensForUser(Long userId) {
        User user = findUserById(userId);
        refreshTokenRepository.deleteByUser(user);
    }

    public User getUserFromRefreshToken(String token) {
        RefreshToken refreshToken = findRefreshTokenByToken(token);
        return refreshToken.getUser();
    }

    private RefreshToken findRefreshTokenByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenRefreshException(REFRESH_TOKEN_NOT_FOUND_MESSAGE + token));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE + userId));
    }
}
