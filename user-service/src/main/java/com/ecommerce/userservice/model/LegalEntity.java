package com.ecommerce.userservice.model;

import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity(name = "legal_entities")
@ToString(exclude = "user")
@EqualsAndHashCode(of = "legalEntityId")
public class LegalEntity {

    @Id
    @Column(name = "user_id")
    private Long legalEntityId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @CNPJ
    @Column(name = "cnpj", unique = true)
    private String cnpj;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "state_registration")
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
