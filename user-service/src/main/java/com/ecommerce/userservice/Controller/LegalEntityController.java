package com.ecommerce.userservice.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.userservice.Service.UserService;
import com.ecommerce.userservice.dto.CreateLegalEntityRequest;
import com.ecommerce.userservice.dto.UserResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/legal-entities")
public class LegalEntityController {

    private final UserService userService;

    public LegalEntityController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> postRegister(@RequestBody @Valid CreateLegalEntityRequest request) {
        UserResponse response = userService.createLegalEntity(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}
