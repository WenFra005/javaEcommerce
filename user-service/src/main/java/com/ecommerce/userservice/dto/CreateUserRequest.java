package com.ecommerce.userservice.dto;

import com.ecommerce.userservice.enums.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Estrutura base para criação de usuários.")
public abstract class CreateUserRequest {

    @Schema(description = "Nome do usuário", example = "João da Silva")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "E-mail do usuário", example = "joao.silva@example.com")
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String userEmail;

    @Schema(description = "Senha do usuário", example = "senha@123")
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String userPassword;

    @Schema(description = "Perfil de acesso do usuário", example = "CLIENTE")
    @NotNull(message = "User role is required")
    private UserRole userRole;

    protected CreateUserRequest() {
    }

    protected CreateUserRequest(String name, String userEmail, String userPassword, UserRole userRole) {
        this.name = name;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userRole = userRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

}
