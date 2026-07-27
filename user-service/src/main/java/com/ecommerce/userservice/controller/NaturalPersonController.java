package com.ecommerce.userservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.userservice.dto.CreateNaturalPersonRequest;
import com.ecommerce.userservice.dto.ErrorResponse;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/natural-persons")
@Tag(name = "Natural Person Controller", description = "Endpoints para gerenciar usuários do tipo Pessoa Física ou Natural")
public class NaturalPersonController {

    private final UserService userService;

    public NaturalPersonController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Registrar Pessoa Física ou Natural", description = "Cria um novo usuário do tipo Pessoa Física ou Natural no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida, dados do usuário não atendem aos critérios de validação", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito, o e-mail fornecido já está em uso", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> postRegister(@RequestBody @Valid CreateNaturalPersonRequest request) {
        UserResponse response = userService.createNaturalPerson(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
