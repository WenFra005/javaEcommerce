package com.ecommerce.userservice.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.userservice.Service.UserService;
import com.ecommerce.userservice.dto.CreateNaturalPersonRequest;
import com.ecommerce.userservice.dto.UserResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/natural-persons")
public class NaturalPersonController {

    private final UserService userService;

    public NaturalPersonController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> postRegister(@RequestBody @Valid CreateNaturalPersonRequest request) {
        UserResponse response = userService.createNaturalPerson(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}
