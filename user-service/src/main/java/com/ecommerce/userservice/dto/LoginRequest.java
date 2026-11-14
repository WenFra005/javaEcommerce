package com.ecommerce.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Dados mínimos necessários para autenticar um usuário na API.
 *
 * <p>
 * O payload carrega apenas as credenciais usadas no fluxo de login; a validação
 * e a troca por tokens ocorrem na camada de serviço.
 *
 * @since 0.1.0
 */
@Schema(description = "Credenciais necessárias para autenticação do usuário.")
public class LoginRequest {

    @Schema(description = "E-mail do usuário", example = "usuario@example.com")
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    @Schema(description = "Senha do usuário", example = "senha@123")
    @NotBlank(message = "Password is required")
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
