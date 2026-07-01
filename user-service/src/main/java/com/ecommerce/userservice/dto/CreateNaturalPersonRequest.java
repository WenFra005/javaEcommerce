package com.ecommerce.userservice.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import com.ecommerce.userservice.Enums.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateNaturalPersonRequest extends CreateUserRequest {

    @CPF(message = "CPF should be valid")
    @NotBlank(message = "CPF is required")
    private String cpf;

    @NotNull(message = "Birth date is required")
    @JsonFormat(pattern = "dd/MM/yyyy")
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
