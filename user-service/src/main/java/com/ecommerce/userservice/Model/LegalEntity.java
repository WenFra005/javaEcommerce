package com.ecommerce.userservice.Model;

import jakarta.persistence.Entity;

@Entity(name = "legal_entities")
public class LegalEntity {

    private Long legalEntityId;

    private User user;

    private String cnpj;

    private String companyName;

    private String stateRegistration;

    public LegalEntity() {
    }

    public LegalEntity(Long legalEntityId, User user, String cnpj, String companyName, String stateRegistration) {
        this.legalEntityId = legalEntityId;
        this.user = user;
        this.cnpj = cnpj;
        this.companyName = companyName;
        this.stateRegistration = stateRegistration;
    }

    public Long getLegalEntityId() {
        return legalEntityId;
    }

    public void setLegalEntityId(Long legalEntityId) {
        this.legalEntityId = legalEntityId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
