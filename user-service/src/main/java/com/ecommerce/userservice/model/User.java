package com.ecommerce.userservice.model;

import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;

import com.ecommerce.userservice.enums.UserRole;
import com.ecommerce.userservice.enums.UserStatus;
import com.ecommerce.userservice.enums.UserType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidade principal de usuário do serviço.
 *
 * <p>
 * Mapeia a tabela {@code users} no PostgreSQL e concentra os dados comuns de
 * autenticação e perfil. A entidade também mantém o vínculo com o detalhe de
 * pessoa física ({@link NaturalPerson}), pessoa jurídica ({@link LegalEntity})
 * e o token de renovação ({@link RefreshToken}), permitindo que o estado do
 * usuário seja tratado de forma centralizada.
 *
 * <p>
 * O tipo de usuário é definido por {@link UserType} e o estado operacional por
 * {@link UserStatus}.
 *
 * @since 0.1.0
 */
@Entity(name = "users")
@ToString(exclude = { "naturalPerson", "legalEntity" })
@EqualsAndHashCode(of = "userEmail")
@Getter
@Setter
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "user_name")
    private String name;

    @Email
    @Column(name = "user_email", unique = true)
    private String userEmail;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "user_status")
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(name = "user_created_at", updatable = false)
    @CreationTimestamp
    private Instant userCreatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = true, fetch = FetchType.LAZY)
    private NaturalPerson naturalPerson;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = true, fetch = FetchType.LAZY)
    private LegalEntity legalEntity;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = true, fetch = FetchType.LAZY)
    private RefreshToken refreshToken;

    public User() {
    }

    public User(Long userId, String name, String userEmail, String userPassword,
            UserStatus userStatus, UserRole userRole, UserType userType, Instant userCreatedAt,
            NaturalPerson naturalPerson, LegalEntity legalEntity) {
        this.userId = userId;
        this.name = name;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userStatus = userStatus;
        this.userRole = userRole;
        this.userType = userType;
        this.userCreatedAt = userCreatedAt;
        this.naturalPerson = naturalPerson;
        this.legalEntity = legalEntity;
    }

    public void setNaturalPerson(NaturalPerson naturalPerson) {
        this.naturalPerson = naturalPerson;
        if (naturalPerson != null && naturalPerson.getUser() != this) {
            naturalPerson.setUser(this);
        }
    }

    public void setLegalEntity(LegalEntity legalEntity) {
        this.legalEntity = legalEntity;
        if (legalEntity != null && legalEntity.getUser() != this) {
            legalEntity.setUser(this);
        }
    }

}
