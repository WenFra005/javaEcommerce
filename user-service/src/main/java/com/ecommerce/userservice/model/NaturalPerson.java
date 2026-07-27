package com.ecommerce.userservice.model;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity(name = "natural_persons")
@ToString(exclude = "user")
@EqualsAndHashCode(of = "naturalPersonId")
public class NaturalPerson {

    @Id
    @Column(name = "natural_person_id")
    private Long naturalPersonId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "natural_person_id")
    private User user;

    @CPF
    @Column(name = "cpf", unique = true)
    private String cpf;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    public NaturalPerson() {
    }

    public NaturalPerson(Long naturalPersonId, User user, @CPF String cpf, LocalDate birthDate) {
        this.naturalPersonId = naturalPersonId;
        this.user = user;
        this.cpf = cpf;
        this.birthDate = birthDate;
    }

    public Long getNaturalPersonId() {
        return naturalPersonId;
    }

    public void setNaturalPersonId(Long naturalPersonId) {
        this.naturalPersonId = naturalPersonId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

}
