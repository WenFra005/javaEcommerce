package com.ecommerce.userservice.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.userservice.Service.UserService;
import com.ecommerce.userservice.dto.CreateAdminRequest;
import com.ecommerce.userservice.dto.UserResponse;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<UserResponse> postCreateAdmin(@RequestBody @Valid CreateAdminRequest request) {
        UserResponse response = userService.createAdmin(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}
