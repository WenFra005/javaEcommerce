package com.ecommerce.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta da renovação de sessão com novos tokens.")
public class RefreshResponse {

    @Schema(description = "Novo token JWT de acesso", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String accessToken;
    @Schema(description = "Novo refresh token", example = "f3fcb3b0-0ce4-4f4f-96af-7e6ff4f63f89")
    private String refreshToken;
    @Schema(description = "Tipo de autenticação do token", example = "Bearer")
    private String tokenType = "Bearer";

    public RefreshResponse() {
    }

    public RefreshResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    

}
