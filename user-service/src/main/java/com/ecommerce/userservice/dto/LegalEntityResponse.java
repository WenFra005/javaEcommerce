package com.ecommerce.userservice.dto;

public class LegalEntityResponse extends UserResponse{

    private String cnpj;

    private String companyName;

    private String stateRegistration;

    public LegalEntityResponse() {
    }

    public LegalEntityResponse(String cnpj, String companyName, String stateRegistration) {
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
