package com.ecommerce.userservice.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({ "userId", "userName", "userEmail", "userStatus", "userRole", "userType", "userCreatedAt", "cpf",
        "birthDate" })
@Schema(description = "Representa a resposta de um usuário do tipo Pessoa Física ou Natural Person, incluindo informações específicas desse tipo de usuário.")
@Getter
@Setter
public class NaturalPersonResponse extends UserResponse {

    private String cpf;

    private LocalDate birthDate;

    public NaturalPersonResponse() {
    }

    public NaturalPersonResponse(String cpf, LocalDate birthDate) {
        this.cpf = cpf;
        this.birthDate = birthDate;
    }

}
