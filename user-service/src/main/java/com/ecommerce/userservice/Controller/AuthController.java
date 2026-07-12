package com.ecommerce.userservice.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.userservice.Model.RefreshToken;
import com.ecommerce.userservice.Model.User;
import com.ecommerce.userservice.Security.CustomUserDetails;
import com.ecommerce.userservice.Security.JwtUtil;
import com.ecommerce.userservice.Service.RefreshTokenService;
import com.ecommerce.userservice.dto.AuthResponse;
import com.ecommerce.userservice.dto.LoginRequest;
import com.ecommerce.userservice.dto.RefreshRequest;
import com.ecommerce.userservice.dto.RefreshResponse;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {

    
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> postLogin(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        String accessToken = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getUser().getUserId());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUserId());

        AuthResponse authResponse = new AuthResponse(
            accessToken, 
            refreshToken.getToken(), 
            "Bearer", 
            user.getUserId(), 
            user.getUserEmail()
        );

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> postLogout(@RequestBody @Valid RefreshRequest request) {
        refreshTokenService.revokeRefreshToken(request.getRefreshToken());

        return ResponseEntity.noContent().build();
    }
    
    
    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> postRefresh(@RequestBody @Valid RefreshRequest request) {
        RefreshToken refreshToken = refreshTokenService.validadeRefreshToken(request.getRefreshToken());

        User user = refreshToken.getUser();

        String newAccessToken = jwtUtil.generateToken(user.getUserEmail(), user.getUserId());
        
        refreshTokenService.revokeRefreshToken(request.getRefreshToken());
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getUserId());

        RefreshResponse refreshResponse = new RefreshResponse(
            newAccessToken,
            newRefreshToken.getToken()
        );

        return ResponseEntity.ok(refreshResponse);
    }
    

}
