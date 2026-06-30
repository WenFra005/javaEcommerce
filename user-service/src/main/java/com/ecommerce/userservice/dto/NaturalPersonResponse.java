package com.ecommerce.userservice.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa a resposta de um usuário do tipo Pessoa Física ou Natural Person, incluindo informações específicas desse tipo de usuário.")
public class NaturalPersonResponse extends UserResponse{

    private String cpf;

    private LocalDate birthDate;

    public NaturalPersonResponse() {
    }

    public NaturalPersonResponse(String cpf, LocalDate birthDate) {
        this.cpf = cpf;
        this.birthDate = birthDate;
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
