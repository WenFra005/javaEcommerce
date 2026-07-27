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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ecommerce.userservice.config.SecurityConfig;
import com.ecommerce.userservice.controller.NaturalPersonController;
import com.ecommerce.userservice.dto.CreateNaturalPersonRequest;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.enums.UserType;
import com.ecommerce.userservice.exception.EmailAlreadyExistsException;
import com.ecommerce.userservice.security.JwtUtil;
import com.ecommerce.userservice.service.CustomUserDetailsService;
import com.ecommerce.userservice.service.UserService;

@WebMvcTest(controllers = NaturalPersonController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
public class NaturalPersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void testPostRegister_WithValidData_SHouldReturnCreated() throws Exception {
        UserResponse mockResponse = new UserResponse();
        mockResponse.setUserId(1L);
        mockResponse.setUserName("Test User");
        mockResponse.setUserEmail("test@email.com");
        mockResponse.setUserType(UserType.PF);

        when(userService.createNaturalPerson(any(CreateNaturalPersonRequest.class))).thenReturn(mockResponse);

        String requestBody = """
                {
                    "name": "Test User",
                    "userEmail": "test@email.com",
                    "userPassword": "senha123",
                    "userRole": "CLIENTE",
                    "cpf": "71136317708",
                    "birthDate": "20/06/2001"
                }
                """;

        mockMvc.perform(post("/natural-persons/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName").value("Test User"))
                .andExpect(jsonPath("$.userEmail").value("test@email.com"));

    }

    @Test
    void testPostRegister_WithInvalidCpf_ShouldReturnBadRequest() throws Exception {
        String requestBody = """
                {
                    "name": "Test User",
                    "userEmail": "test@email.com",
                    "userPassword": "senha123",
                    "userRole": "CLIENTE",
                    "cpf": "12345678901",
                    "birthDate": "20/06/2001"
                }
                """;

        mockMvc.perform(post("/natural-persons/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("cpf: CPF should be valid"));

    }

    @Test
    void testPostRegister_WithDuplicateEmail_ShouldReturnBadRequest() throws Exception {
        when(userService.createNaturalPerson(any(CreateNaturalPersonRequest.class)))
                .thenThrow(new EmailAlreadyExistsException("Email já cadastrado"));

        String requestBody = """
                {
                    "name": "Test User",
                    "userEmail": "test@email.com",
                    "userPassword": "senha123",
                    "userRole": "CLIENTE",
                    "cpf": "71136317708",
                    "birthDate": "20/06/2001"
                }
                """;

        mockMvc.perform(post("/natural-persons/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email já cadastrado"));

    }

    @Test
    void testPostRegister_WithMissingFields_ShouldReturnBadRequest() throws Exception {
        String requestBody = """
                {
                    "name": "",
                    "userEmail": "",
                    "userPassword": "",
                    "userRole": "",
                    "cpf": "",
                    "birthDate": ""
                }
                """;

        mockMvc.perform(post("/natural-persons/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

}