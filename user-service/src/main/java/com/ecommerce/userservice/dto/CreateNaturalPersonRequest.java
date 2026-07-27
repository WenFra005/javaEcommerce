package com.ecommerce.userservice.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import com.ecommerce.userservice.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para criação de usuário do tipo pessoa física.")
public class CreateNaturalPersonRequest extends CreateUserRequest {

    @Schema(description = "CPF do usuário", example = "123.456.789-09")
    @CPF(message = "CPF should be valid")
    @NotBlank(message = "CPF is required")
    private String cpf;

    @Schema(description = "Data de nascimento do usuário", example = "31/12/1990")
    @NotNull(message = "Birth date is required")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    public CreateNaturalPersonRequest() {
        super();
    }

    public CreateNaturalPersonRequest(String name, String userEmail, String userPassword, UserRole userRole, String cpf,
            LocalDate birthDate) {
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

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

}
