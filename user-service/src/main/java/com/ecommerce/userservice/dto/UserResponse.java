package com.ecommerce.userservice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ecommerce.userservice.Enums.UserRole;
import com.ecommerce.userservice.Enums.UserStatus;
import com.ecommerce.userservice.Enums.UserType;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    
    @Schema(description = "CPF do usuário (para Pessoa Física)", example = "123.456.789-00")
    private String cpf;

    @Schema(description = "Data de nascimento do usuário (para Pessoa Física)", example = "1990-01-01")
    private LocalDate birthDate;

    @Schema(description = "CNPJ do usuário (para Pessoa Jurídica)", example = "12.345.678/0001-00")
    private String cnpj;

    @Schema(description = "Nome da empresa (para Pessoa Jurídica)", example = "Empresa Exemplo LTDA")
    private String companyName;

    @Schema(description = "Inscrição estadual (para Pessoa Jurídica)", example = "123.456.789.000")
    private String stateRegistration;

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStateRegistration() {
        return stateRegistration;
    }

    public void setStateRegistration(String stateRegistration) {
        this.stateRegistration = stateRegistration;
    }

}
