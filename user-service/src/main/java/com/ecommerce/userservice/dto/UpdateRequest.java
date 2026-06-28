package com.ecommerce.userservice.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

public class UpdateRequest {

    private String name;
    private String userEmail;
    private String userPassword;

    @CPF(message = "CPF should be valid")
    private String cpf;
    private LocalDate birthDate;

    @CNPJ(message = "CNPJ should be valid")
    private String cnpj;
    private String companyName;
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
