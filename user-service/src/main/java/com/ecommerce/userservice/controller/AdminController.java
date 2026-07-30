package com.ecommerce.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.userservice.dto.CreateAdminRequest;
import com.ecommerce.userservice.dto.ErrorResponse;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Controller", description = "Endpoints administrativos para criação de usuários com perfil de administrador.")
public class AdminController {

    private UserService userService;

    public AdminController() {

    }

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Criar administrador", description = "Cria um novo usuário com perfil ADMIN. Requer autenticação de administrador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Administrador criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Payload inválido ou malformado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário autenticado sem permissão de administrador", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de dados, como e-mail já cadastrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "******")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<UserResponse> postCreateAdmin(@RequestBody @Valid CreateAdminRequest request) {
        UserResponse response = userService.createAdmin(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
