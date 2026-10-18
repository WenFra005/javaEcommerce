package com.ecommerce.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload usado para operações de refresh e logout baseadas em refresh token.")
public class RefreshRequest {

    @Schema(description = "Refresh token ativo", example = "f3fcb3b0-0ce4-4f4f-96af-7e6ff4f63f89")
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;

    public RefreshRequest() {
    }

    public RefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
