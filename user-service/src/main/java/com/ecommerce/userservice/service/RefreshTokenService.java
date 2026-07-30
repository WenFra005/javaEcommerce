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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElse(new RefreshToken());

        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));

        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenRefreshException("Refresh token not found: " + token));

        if (refreshToken.isRevoked()) {
            throw new TokenRefreshException("Refresh token has been revoked: " + token);
        }

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new TokenRefreshException("Refresh token has expired: " + token);
        }

        return refreshToken;
    };

    @Transactional
    public void revokeRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenRefreshException("Refresh token not found: " + token));

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void revokeAllRefreshTokensForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        refreshTokenRepository.deleteByUser(user);
    }

    public User getUserFromRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenRefreshException("Refresh token not found: " + token));
        return refreshToken.getUser();
    }

}
