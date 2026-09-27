package com.ecommerce.userservice.model;

import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidade de pessoa jurídica vinculada a um {@link User}.
 *
 * <p>
 * Mapeia a tabela {@code legal_entities} no PostgreSQL e concentra os dados
 * específicos de uma pessoa jurídica. O identificador é compartilhado com a
 * entidade {@link User} para manter o vínculo um-para-um consistente no banco.
 *
 * @since 0.1.0
 */
@Entity(name = "legal_entities")
@ToString(exclude = "user")
@EqualsAndHashCode(of = "legalEntityId")
@Getter
@Setter
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

}
