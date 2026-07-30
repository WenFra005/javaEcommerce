package com.ecommerce.userservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.userservice.dto.AuthResponse;
import com.ecommerce.userservice.dto.ErrorResponse;
import com.ecommerce.userservice.dto.LoginRequest;
import com.ecommerce.userservice.dto.RefreshRequest;
import com.ecommerce.userservice.dto.RefreshResponse;
import com.ecommerce.userservice.model.RefreshToken;
import com.ecommerce.userservice.model.User;
import com.ecommerce.userservice.security.CustomUserDetails;
import com.ecommerce.userservice.security.JwtUtil;
import com.ecommerce.userservice.service.RefreshTokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "Endpoints de autenticação, revogação e renovação de tokens.")
public class AuthController {

        private AuthenticationManager authenticationManager;
        private RefreshTokenService refreshTokenService;
        private JwtUtil jwtUtil;

        public AuthController() {

        }

        @Autowired
        public AuthController(AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService,
                        JwtUtil jwtUtil) {
                this.authenticationManager = authenticationManager;
                this.refreshTokenService = refreshTokenService;
                this.jwtUtil = jwtUtil;
        }

        @Operation(summary = "Autenticar usuário", description = "Valida credenciais e retorna access token JWT e refresh token.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Payload inválido ou malformado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PostMapping("/login")
        public ResponseEntity<AuthResponse> postLogin(@RequestBody LoginRequest loginRequest) {

                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                                                loginRequest.getPassword()));

                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

                User user = userDetails.getUser();

                String accessToken = jwtUtil.generateToken(userDetails.getUsername(),
                                userDetails.getUser().getUserId());

                RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUserId());

                AuthResponse authResponse = new AuthResponse(
                                accessToken,
                                refreshToken.getToken(),
                                "Bearer",
                                user.getUserId(),
                                user.getUserEmail());

                return ResponseEntity.ok(authResponse);
        }

        @Operation(summary = "Logout do usuário", description = "Revoga o refresh token informado, encerrando a sessão ativa.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Logout realizado com sucesso", content = @Content),
                        @ApiResponse(responseCode = "400", description = "Payload inválido (ex.: refresh token ausente)", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "403", description = "Refresh token inválido, expirado, revogado ou inexistente", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PostMapping("/logout")
        public ResponseEntity<Void> postLogout(@RequestBody @Valid RefreshRequest request) {
                refreshTokenService.revokeRefreshToken(request.getRefreshToken());

                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Renovar sessão", description = "Valida o refresh token atual, emite novo access token e novo refresh token.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Tokens renovados com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RefreshResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Payload inválido (ex.: refresh token ausente)", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "403", description = "Refresh token inválido, expirado, revogado ou inexistente", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PostMapping("/refresh")
        public ResponseEntity<RefreshResponse> postRefresh(@RequestBody @Valid RefreshRequest request) {
                RefreshToken refreshToken = refreshTokenService.validateRefreshToken(request.getRefreshToken());

                User user = refreshToken.getUser();

                String newAccessToken = jwtUtil.generateToken(user.getUserEmail(), user.getUserId());

                refreshTokenService.revokeRefreshToken(request.getRefreshToken());
                RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getUserId());

                RefreshResponse refreshResponse = new RefreshResponse(
                                newAccessToken,
                                newRefreshToken.getToken());

                return ResponseEntity.ok(refreshResponse);
        }

}
