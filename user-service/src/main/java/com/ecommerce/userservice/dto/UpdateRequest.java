package com.ecommerce.userservice.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Campos disponíveis para atualização parcial de um usuário.")
public class UpdateRequest {

    @Schema(description = "Novo nome do usuário", example = "João da Silva Atualizado")
    private String name;
    @Schema(description = "Novo e-mail do usuário", example = "joao.atualizado@example.com")
    private String userEmail;
    @Schema(description = "Nova senha do usuário", example = "novaSenha@123")
    private String userPassword;

    @Schema(description = "CPF do usuário para atualização de dados de pessoa física", example = "123.456.789-09")
    @CPF(message = "CPF should be valid")
    private String cpf;
    @Schema(description = "Data de nascimento para atualização de dados de pessoa física", example = "1990-12-31")
    private LocalDate birthDate;

    @Schema(description = "CNPJ da empresa para atualização de dados de pessoa jurídica", example = "12.345.678/0001-99")
    @CNPJ(message = "CNPJ should be valid")
    private String cnpj;
    @Schema(description = "Razão social da empresa", example = "Empresa Exemplo Atualizada LTDA")
    private String companyName;
    @Schema(description = "Inscrição estadual da empresa", example = "987654321")
    private String stateRegistration;

    public UpdateRequest() {
    }

    public UpdateRequest(String name, String userEmail, String userPassword, String cpf, LocalDate birthDate, String cnpj, String companyName, String stateRegistration) {
        this.name = name;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.cpf = cpf;
        this.birthDate = birthDate;
        this.cnpj = cnpj;
        this.companyName = companyName;
        this.stateRegistration = stateRegistration;
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
