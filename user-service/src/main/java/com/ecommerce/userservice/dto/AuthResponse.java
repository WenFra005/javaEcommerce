package com.ecommerce.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticação com tokens e dados básicos do usuário autenticado.")
public class AuthResponse {

    @Schema(description = "Token JWT de acesso", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String accessToken;
    @Schema(description = "Token para renovação de sessão", example = "f3fcb3b0-0ce4-4f4f-96af-7e6ff4f63f89")
    private String refreshToken;
    @Schema(description = "Tipo de autenticação do token", example = "Bearer")
    private String tokenType = "Bearer";
    @Schema(description = "Identificador único do usuário autenticado", example = "1")
    private Long userId;
    @Schema(description = "E-mail do usuário autenticado", example = "usuario@example.com")
    private String userEmail;
    
    public AuthResponse(String accessToken, String refreshToken, String tokenType, Long userId, String userEmail) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.userId = userId;
        this.userEmail = userEmail;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

}
