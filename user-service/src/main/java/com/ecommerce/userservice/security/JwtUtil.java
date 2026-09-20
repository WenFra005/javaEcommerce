package com.ecommerce.userservice.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Responsável pela criação e validação dos tokens JWT da aplicação.
 *
 * <p>
 * Encapsula as regras de assinatura, expiração e extração de claims usadas no
 * fluxo de autenticação e renovação de sessão.
 *
 * @since 1.0
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    /**
     * Gera a chave de assinatura a partir do segredo configurado.
     *
     * @return chave simétrica usada para assinar e validar tokens.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Gera um JWT com os dados mínimos necessários para autenticar o usuário.
     *
     * @param email  sujeito do token.
     * @param userId identificador do usuário incluído como claim.
     * @return o token assinado e com prazo de expiração configurado.
     */
    public String generateToken(String email, Long userId) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(jwtExpirationMs)))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrai um claim específico de um token assinado.
     *
     * @param token          token JWT assinado.
     * @param claimsResolver função que seleciona o claim desejado.
     * @param <T>            tipo do valor retornado pelo claim.
     * @return valor extraído do token.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    /**
     * Extrai o e-mail armazenado como subject do token.
     *
     * @param token token JWT.
     * @return e-mail embutido no token.
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrai o identificador do usuário armazenado no token.
     *
     * @param token token JWT.
     * @return identificador persistido como claim.
     */
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    /**
     * Extrai o instante de expiração do token.
     *
     * @param token token JWT.
     * @return instante em que o token expira.
     */
    public Instant extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration).toInstant();
    }

    /**
     * Informa se o token já expirou.
     *
     * @param token token JWT.
     * @return {@code true} quando o token está expirado.
     */
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).isBefore(Instant.now());
    }

    /**
     * Valida se o token pertence ao usuário informado e ainda é válido.
     *
     * @param token     token JWT.
     * @param userEmail e-mail esperado para o token.
     * @return {@code true} quando o token é consistente e não expirou.
     */
    public Boolean validateToken(String token, String userEmail) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(userEmail) && !isTokenExpired(token));
    }

}
