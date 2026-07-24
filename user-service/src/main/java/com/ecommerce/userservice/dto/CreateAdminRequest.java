package com.ecommerce.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados necessários para criação de um usuário administrador.")
public class CreateAdminRequest {

    @Schema(description = "Nome do administrador", example = "Administrador Principal")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "E-mail do administrador", example = "admin@example.com")
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(description = "Senha do administrador", example = "admin@12345")
    @NotBlank(message = "Password is required")
    private String password;

    public CreateAdminRequest() {
    }

    public CreateAdminRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
