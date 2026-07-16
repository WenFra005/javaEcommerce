package com.ecommerce.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credenciais necessárias para autenticação do usuário.")
public class LoginRequest {

    @Schema(description = "E-mail do usuário", example = "usuario@example.com")
    private String email;
    @Schema(description = "Senha do usuário", example = "senha@123")
    private String password;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


}
