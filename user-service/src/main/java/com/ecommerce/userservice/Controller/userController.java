package com.ecommerce.userservice.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.userservice.Security.CustomUserDetails;
import com.ecommerce.userservice.Security.JwtUtil;
import com.ecommerce.userservice.Service.UserService;
import com.ecommerce.userservice.dto.CreateUserRequest;
import com.ecommerce.userservice.dto.UpdateResponse;
import com.ecommerce.userservice.dto.UserResponse;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/users")
public class userController {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    public userController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id, Authentication authentication) {
        String authenticatedUserEmail = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
        UserResponse userResponse = userService.findUserById(id, authenticatedUserEmail);

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponse> postUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse userResponse = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<UpdateResponse> putUser(@Valid @RequestBody CreateUserRequest request, @PathVariable Long id, Authentication authentication) {

        String authenticatedUserEmail = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
        UserResponse updatedUser = userService.updateUser(id, request, authenticatedUserEmail);

        String newToken = jwtUtil.generateToken(updatedUser.getUserEmail(), id);
        UpdateResponse updateResponse = new UpdateResponse(updatedUser, newToken);

        return ResponseEntity.ok(updateResponse);
    }

    @DeleteMapping("/delete/{id}") 
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, Authentication authentication) {
        String authenticatedUserEmail = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
        userService.deleteUser(id, authenticatedUserEmail);

        return ResponseEntity.noContent().build();  
    }
}
