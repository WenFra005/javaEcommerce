package com.ecommerce.userservice.dto;

import org.hibernate.validator.constraints.br.CNPJ;

import com.ecommerce.userservice.Enums.UserRole;

import jakarta.validation.constraints.NotBlank;

public class CreateLegalEntityRequest extends CreateUserRequest {

    @CNPJ(message = "CNPJ sholuld be valid")
    @NotBlank(message = "CNPJ is required")
    private String cnpj;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "State registration is required")
    private String stateRegistration;

    public CreateLegalEntityRequest() {
        super();
    }

    public CreateLegalEntityRequest(String name, String userEmail, String userPassword, UserRole userRole, String cnpj, String companyName, String stateRegistration) {
        super(name, userEmail, userPassword, userRole);
        this.cnpj = cnpj;
        this.companyName = companyName;
        this.stateRegistration = stateRegistration;
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
