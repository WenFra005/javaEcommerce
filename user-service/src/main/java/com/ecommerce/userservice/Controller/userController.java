package com.ecommerce.userservice.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.userservice.Enums.UserRole;
import com.ecommerce.userservice.Security.CustomUserDetails;
import com.ecommerce.userservice.Security.JwtUtil;
import com.ecommerce.userservice.Service.UserService;
import com.ecommerce.userservice.dto.CreateUserRequest;
import com.ecommerce.userservice.dto.UpdateRequest;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.dto.UpdateResponse;

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
        String email = authentication.getName();
        UserRole role = ((CustomUserDetails) authentication.getPrincipal()).getUser().getUserRole();
        UserResponse response = userService.findUserById(id, email, role);

        return ResponseEntity.ok(response);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<UpdateResponse> putUser(@Valid @RequestBody UpdateRequest request, @PathVariable Long id, Authentication authentication) {

        String email = authentication.getName();
        UserRole role = ((CustomUserDetails) authentication.getPrincipal()).getUser().getUserRole();
        UserResponse updated = userService.updateUser(id, request, email, role);

        String newToken = null;
        if (request.getUserEmail() != null && !request.getUserEmail().isBlank()) {
            newToken = jwtUtil.generateToken(updated.getUserName(), updated.getUserId());
        }

        return ResponseEntity.ok(new UpdateResponse(updated, newToken));
    }

    @DeleteMapping("/delete/{id}") 
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        UserRole role = ((CustomUserDetails) authentication.getPrincipal()).getUser().getUserRole();
        userService.deleteUser(id, email, role);

        return ResponseEntity.noContent().build();  
    }
}
