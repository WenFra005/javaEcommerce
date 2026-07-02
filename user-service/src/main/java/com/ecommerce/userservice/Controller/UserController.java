package com.ecommerce.userservice.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.userservice.Enums.UserRole;
import com.ecommerce.userservice.Security.CustomUserDetails;
import com.ecommerce.userservice.Security.JwtUtil;
import com.ecommerce.userservice.Service.UserService;
import com.ecommerce.userservice.dto.ErrorResponse;
import com.ecommerce.userservice.dto.UpdateRequest;
import com.ecommerce.userservice.dto.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.ecommerce.userservice.dto.UpdateResponse;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Operações gerais para usuários, incluindo registro, atualização e exclusão de contas.")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna os dados de um usuário específico. Apenas o próprio usuário ou um administrador pode acessar essas informações.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado, o usuário autenticado não tem permissão para acessar os dados de outro usuário", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado com o ID fornecido", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        UserRole role = ((CustomUserDetails) authentication.getPrincipal()).getUser().getUserRole();
        UserResponse response = userService.findUserById(id, email, role);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário específico. Apenas o próprio usuário ou um administrador pode realizar essa operação.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateResponse.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado, o usuário autenticado não tem permissão para atualizar os dados de outro usuário", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado com o ID fornecido", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/update/{id}")
    public ResponseEntity<UpdateResponse> putUser(@Valid @RequestBody UpdateRequest request, @PathVariable Long id, Authentication authentication) {

        String email = authentication.getName();
        UserRole role = ((CustomUserDetails) authentication.getPrincipal()).getUser().getUserRole();
        UserResponse updated = userService.updateUser(id, request, email, role);

        String newToken = null;
        if (request.getUserEmail() != null && !request.getUserEmail().isBlank()) {
            newToken = jwtUtil.generateToken(updated.getUserEmail(), updated.getUserId());
        }

        return ResponseEntity.ok(new UpdateResponse(updated, newToken));
    }

    @Operation(summary = "Excluir usuário", description = "Exclui um usuário específico. Apenas o próprio usuário ou um administrador pode realizar essa operação.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado, o usuário autenticado não tem permissão para excluir outro usuário", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado com o ID fornecido", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/delete/{id}") 
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        UserRole role = ((CustomUserDetails) authentication.getPrincipal()).getUser().getUserRole();
        userService.deleteUser(id, email, role);

        return ResponseEntity.noContent().build();  
    }
}
