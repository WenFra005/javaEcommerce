package com.ecommerce.userservice.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({ "userId", "userName", "userEmail", "userStatus", "userRole", "userType", "userCreatedAt", "cnpj",
        "companyName", "stateRegistration" })
@Schema(description = "Resposta de usuário do tipo pessoa jurídica com dados específicos da empresa.")
public class LegalEntityResponse extends UserResponse{

    @Schema(description = "CNPJ da empresa", example = "12.345.678/0001-99")
    private String cnpj;

    @Schema(description = "Razão social da empresa", example = "Empresa Exemplo LTDA")
    private String companyName;

    @Schema(description = "Inscrição estadual da empresa", example = "123456789")
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
