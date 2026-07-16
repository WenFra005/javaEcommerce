package com.ecommerce.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta da atualização de usuário, com dados atualizados e token opcional.")
public class UpdateResponse {

    @Schema(description = "Dados atualizados do usuário")
    private UserResponse userResponse;
    @Schema(description = "Novo token JWT quando há alteração de e-mail; pode ser nulo", nullable = true, example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;
    
    public UpdateResponse(UserResponse userResponse, String token) {
        this.userResponse = userResponse;
        this.token = token;
    }

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public String getToken() {
        return token;
    }

    

}
