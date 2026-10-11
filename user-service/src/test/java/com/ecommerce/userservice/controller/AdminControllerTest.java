package com.ecommerce.userservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ecommerce.userservice.config.SecurityConfig;
import com.ecommerce.userservice.dto.CreateAdminRequest;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.enums.UserRole;
import com.ecommerce.userservice.enums.UserType;
import com.ecommerce.userservice.exception.EmailAlreadyExistsException;
import com.ecommerce.userservice.security.JwtUtil;
import com.ecommerce.userservice.service.CustomUserDetailsService;
import com.ecommerce.userservice.service.UserService;

@WebMvcTest(controllers = AdminController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostCreateAdmin_AsAdmin_ShouldReturnCreated() throws Exception {
        UserResponse response = new UserResponse();
        response.setUserId(1L);
        response.setUserEmail("admin@email.com");
        response.setUserRole(UserRole.ADMIN);
        response.setUserType(UserType.SYSTEM);

        when(userService.createAdmin(any(CreateAdminRequest.class))).thenReturn(response);

        String requestBody = """
                {
                    "name": "Admin",
                    "email": "admin@example.com",
                    "password": "senha123"
                }
                """;

        mockMvc.perform(post("/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userRole").value("ADMIN"));

    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void testPostCreateAdmin_AsCliente_ShouldReturnForbiden() throws Exception {
        String requestBody = """
                {
                    "name": "Admin",
                    "email": "admin@example.com",
                    "password": "senha123"
                }
                """;
        mockMvc.perform(post("/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isForbidden());
    }

    @Test
    void testPostCreateAdmin_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
        String requestBody = """
                {
                    "name": "Admin",
                    "email": "admin@example.com",
                    "password": "senha123"
                }
                """;
        mockMvc.perform(post("/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostCreateAdmin_WithDuplicateEmail_ShouldReturnConflict() throws Exception {
        when(userService.createAdmin(any(CreateAdminRequest.class)))
                .thenThrow(new EmailAlreadyExistsException("Email already exists"));

        String requestBody = """
                {
                    "name": "Admin",
                    "email": "admin@example.com",
                    "password": "senha123"
                }
                """;

        mockMvc.perform(post("/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testPostCreateAdmin_WithMissingFields_ShouldReturnBadRequest() throws Exception {
        String requestBody = """
                {
                    "name": "",
                    "email": "admin@example.com",
                    "password": "senha123"
                }
                """;

        mockMvc.perform(post("/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

}