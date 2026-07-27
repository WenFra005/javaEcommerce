package com.ecommerce.userservice.dto;

import org.hibernate.validator.constraints.br.CNPJ;

import com.ecommerce.userservice.enums.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para criação de usuário do tipo pessoa jurídica.")
public class CreateLegalEntityRequest extends CreateUserRequest {

    @Schema(description = "CNPJ da empresa", example = "12.345.678/0001-99")
    @CNPJ(message = "CNPJ should be valid")
    @NotBlank(message = "CNPJ is required")
    private String cnpj;

    @Schema(description = "Razão social da empresa", example = "Empresa Exemplo LTDA")
    @NotBlank(message = "Company name is required")
    private String companyName;

    @Schema(description = "Inscrição estadual da empresa", example = "123456789")
    @NotBlank(message = "State registration is required")
    private String stateRegistration;

    public CreateLegalEntityRequest() {
        super();
    }

    public CreateLegalEntityRequest(String name, String userEmail, String userPassword, UserRole userRole, String cnpj,
            String companyName, String stateRegistration) {
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
