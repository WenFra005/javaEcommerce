package com.ecommerce.userservice.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import com.ecommerce.userservice.Enums.UserRole;

import jakarta.validation.constraints.NotBlank;

public class CreateNaturalPersonRequest extends CreateUserRequest {

    @CPF(message = "CPF should be valid")
    @NotBlank(message = "CPF is required")
    private String cpf;

    @NotBlank(message = "Birth date is required")
    private LocalDate birthDate;

    public CreateNaturalPersonRequest() {
        super();
    }

    public CreateNaturalPersonRequest(String name, String userEmail, String userPassword, UserRole userRole, String cpf, LocalDate birthDate) {
        super(name, userEmail, userPassword, userRole);
        this.cpf = cpf;
        this.birthDate = birthDate;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

}
