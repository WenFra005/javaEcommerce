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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ecommerce.userservice.config.SecurityConfig;
import com.ecommerce.userservice.dto.CreateLegalEntityRequest;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.enums.UserType;
import com.ecommerce.userservice.exception.EmailAlreadyExistsException;
import com.ecommerce.userservice.exception.ValidationException;
import com.ecommerce.userservice.security.JwtUtil;
import com.ecommerce.userservice.service.CustomUserDetailsService;
import com.ecommerce.userservice.service.UserService;

@WebMvcTest(controllers = LegalEntityController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class LegalEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void testPostRegister_WithValidData_ShouldReturnCreated() throws Exception {
        UserResponse response = new UserResponse();
        response.setUserId(1L);
        response.setUserEmail("company@email.com");
        response.setUserType(UserType.PJ);

        when(userService.createLegalEntity(any(CreateLegalEntityRequest.class))).thenReturn(response);

        String requestBody = """
                    {
                        "name": "test user",
                        "userEmail": "company@email.com",
                        "userPassword": "senha123",
                        "type": "PJ",
                        "userRole": "FORNECEDOR",
                        "cnpj": "02528458000143",
                        "companyName": "Empresa XYZ",
                        "stateRegistration": "680109170648"
                    }
                """;

        mockMvc.perform(post("/legal-entities/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userEmail").value("company@email.com"));

    }

    @Test
    void testPostRegister_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        String requestBody = """
                    {
                        "name": "test user",
                        "userEmail": "invalid-email",
                        "userPassword": "senha123",
                        "type": "PJ",
                        "userRole": "FORNECEDOR",
                        "cnpj": "02528458000143",
                        "companyName": "Empresa XYZ",
                        "stateRegistration": "680109170648"
                    }
                """;

        mockMvc.perform(post("/legal-entities/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("userEmail: Email should be valid"));

    }

    @Test
    void testPostRegister_WithInvalidCnpj_ShouldReturnBadRequest() throws Exception {
        String requestBody = """
                    {
                        "name": "test user",
                        "userEmail": "company@email.com",
                        "userPassword": "senha123",
                        "type": "PJ",
                        "userRole": "FORNECEDOR",
                        "cnpj": "12345678901234",
                        "companyName": "Empresa XYZ",
                        "stateRegistration": "680109170648"
                    }
                """;

        mockMvc.perform(post("/legal-entities/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("cnpj: CNPJ should be valid"));

    }

    @Test
    void testPostRegister_WithExistingCNPJ_ShouldReturnBadRequest() throws Exception {
        when(userService.createLegalEntity(any(CreateLegalEntityRequest.class)))
                .thenThrow(new ValidationException("CNPJ already exists"));

        String requestBody = """
                    {
                        "name": "test user",
                        "userEmail": "company@email.com",
                        "userPassword": "senha123",
                        "type": "PJ",
                        "userRole": "FORNECEDOR",
                        "cnpj": "02528458000143",
                        "companyName": "Empresa XYZ",
                        "stateRegistration": "680109170648"
                    }
                """;

        mockMvc.perform(post("/legal-entities/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("CNPJ already exists"));
    }

    @Test
    void testPostRegister_WithExistingEmail_ShouldReturnConflict() throws Exception {
        when(userService.createLegalEntity(any(CreateLegalEntityRequest.class)))
                .thenThrow(new EmailAlreadyExistsException("Email already exists"));

        String requestBody = """
                    {
                        "name": "test user",
                        "userEmail": "company@email.com",
                        "userPassword": "senha123",
                        "type": "PJ",
                        "userRole": "FORNECEDOR",
                        "cnpj": "02528458000143",
                        "companyName": "Empresa XYZ",
                        "stateRegistration": "680109170648"
                    }
                """;

        mockMvc.perform(post("/legal-entities/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }

    @Test
    void testPostRegister_WithMissingFields_ShouldReturnBadRequest() throws Exception {
        String requestBody = """
                {
                    "name": "",
                    "userEmail": "company@email.com",
                    "userPassword": "senha123"
                }
                """;

        mockMvc.perform(post("/legal-entities/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
