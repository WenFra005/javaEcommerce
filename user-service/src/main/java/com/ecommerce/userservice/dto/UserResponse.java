package com.ecommerce.userservice.dto;

import java.time.LocalDateTime;

import com.ecommerce.userservice.Enums.UserRole;
import com.ecommerce.userservice.Enums.UserStatus;
import com.ecommerce.userservice.Enums.UserType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "userType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = NaturalPersonResponse.class, name = "PF"),
    @JsonSubTypes.Type(value = LegalEntityResponse.class, name = "PJ")
})
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

    @Schema(description = "Função do usuário", example = "CUSTOMER", allowableValues = {"ADMIN", "VENDEDOR", "CLIENTE", "FORNECEDOR"})
    private UserRole userRole;

    @Schema(description = "Tipo do usuário", example = "PF", allowableValues = {"PF", "PJ"})
    private UserType userType;

    @Schema(description = "Data e hora de criação do usuário", example = "2023-10-01T12:34:56", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime userCreatedAt;

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

    public LocalDateTime getUserCreatedAt() {
        return userCreatedAt;
    }

    public void setUserCreatedAt(LocalDateTime userCreatedAt) {
        this.userCreatedAt = userCreatedAt;
    }

}
