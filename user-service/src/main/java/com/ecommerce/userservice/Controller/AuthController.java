package com.ecommerce.userservice.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.userservice.Security.CustomUserDetails;
import com.ecommerce.userservice.Security.JwtUtil;
import com.ecommerce.userservice.dto.AuthResponse;
import com.ecommerce.userservice.dto.LoginRequest;

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
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> postLogin(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(), 
            loginRequest. getPassword()
        );

        Authentication authenticated = authenticationManager.authenticate(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authenticated.getPrincipal();

        String token = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getUser().getUserId());

        return ResponseEntity.ok(new AuthResponse(token, "Bearer", userDetails.getUser().getUserId(), userDetails.getUsername()));
    }
    

}
