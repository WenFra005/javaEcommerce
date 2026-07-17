package com.ecommerce.userservice.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.userservice.Service.UserService;
import com.ecommerce.userservice.dto.CreateLegalEntityRequest;
import com.ecommerce.userservice.dto.ErrorResponse;
import com.ecommerce.userservice.dto.UserResponse;

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
@RequestMapping("/legal-entities")
@Tag(name = "Legal Entity Controller", description = "Endpoints para gerenciar usuários do tipo Pessoa Jurídica ou Legal Entity")
public class LegalEntityController {

    private final UserService userService;

    public LegalEntityController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Registrar Pessoa Jurídica ou Legal Entity", description = "Cria um novo usuário do tipo Pessoa Jurídica ou Legal Entity no sistema.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos, payload malformado ou CNPJ já cadastrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Conflito de dados, como e-mail já cadastrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> postRegister(@RequestBody @Valid CreateLegalEntityRequest request) {
        UserResponse response = userService.createLegalEntity(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}
