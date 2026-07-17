package com.ecommerce.userservice.dto;

import java.time.Instant;
import com.ecommerce.userservice.Enums.UserRole;
import com.ecommerce.userservice.Enums.UserStatus;
import com.ecommerce.userservice.Enums.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representa a resposta de um usuário, incluindo informações gerais e específicas de acordo com o tipo de usuário.")
public class UserResponse {

    @Schema(description = "ID único do usuário", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Schema(description = "Nome do usuário", example = "João da Silva")
    private String userName;
    
    @Schema(description = "Email do usuário", example = "joao.silva@example.com")
    private String userEmail;

    @Schema(description = "Status do usuário", example = "ATIVO", allowableValues = {"ATIVO", "INATIVO", "SUSPENSO"})
    private UserStatus userStatus;

    @Schema(description = "Função do usuário", example = "CLIENTE", allowableValues = {"ADMIN", "VENDEDOR", "CLIENTE", "FORNECEDOR"})
    private UserRole userRole;

    @Schema(description = "Tipo do usuário", example = "PF", allowableValues = {"PF", "PJ", "SYSTEM"})
    private UserType userType;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Data e hora de criação do usuário", example = "2026-07-17T14:38:41Z", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant userCreatedAt;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Instant getUserCreatedAt() {
        return userCreatedAt;
    }

    public void setUserCreatedAt(Instant userCreatedAt) {
        this.userCreatedAt = userCreatedAt;
    }

}
