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

/**
 * Gerencia o ciclo de vida dos tokens de renovação.
 *
 * <p>
 * A classe concentra a criação, validação, revogação e recuperação do usuário
 * associado a um refresh token, mantendo a lógica de sessão fora dos
 * controladores.
 *
 * @since 1.0
 */
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

    /**
     * Cria ou substitui o refresh token associado a um usuário.
     *
     * <p>
     * O novo token recebe um identificador aleatório e prazo de expiração
     * definido pela configuração {@code jwt.refreshExpirationMs}.
     *
     * @param userId identificador do usuário dono do token.
     * @return o refresh token persistido.
     * @throws UserNotFoundException quando o usuário não existir.
     */
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

    /**
     * Valida se um refresh token ainda pode ser usado.
     *
     * @param token valor do token a ser validado.
     * @return o token persistido quando estiver válido.
     * @throws TokenRefreshException quando o token não existir, estiver revogado
     *                               ou expirado.
     */
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

    /**
     * Revoga um refresh token existente.
     *
     * @param token valor do token a ser revogado.
     * @throws TokenRefreshException quando o token não existir.
     */
    @Transactional
    public void revokeRefreshToken(String token) {
        RefreshToken refreshToken = findRefreshTokenByToken(token);
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    /**
     * Remove todos os refresh tokens vinculados a um usuário.
     *
     * @param userId identificador do usuário.
     * @throws UserNotFoundException quando o usuário não existir.
     */
    @Transactional
    public void revokeAllRefreshTokensForUser(Long userId) {
        User user = findUserById(userId);
        refreshTokenRepository.deleteByUser(user);
    }

    /**
     * Recupera o usuário proprietário de um refresh token.
     *
     * @param token valor do token.
     * @return o usuário vinculado ao token.
     * @throws TokenRefreshException quando o token não existir.
     */
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
