package com.ecommerce.userservice.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ecommerce.userservice.Enums.UserRole;
import com.ecommerce.userservice.Enums.UserType;
import com.ecommerce.userservice.Model.User;
import com.ecommerce.userservice.Security.CustomUserDetails;
import com.ecommerce.userservice.Security.JwtUtil;
import com.ecommerce.userservice.Service.CustomUserDetailsService;
import com.ecommerce.userservice.Service.UserService;
import com.ecommerce.userservice.dto.UserResponse;

@WebMvcTest(
    controllers = UserController.class,
    excludeAutoConfiguration = {
        SecurityAutoConfiguration.class, 
        UserDetailsServiceAutoConfiguration.class
    }
)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    private UserResponse createUserResponse() {
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(1L);
        userResponse.setUserEmail("user@email.com");
        userResponse.setUserRole(UserRole.CLIENTE);
        userResponse.setUserType(UserType.PF);
        
        return userResponse;
    }

    private Authentication createMockAuthentication(UserRole role) {
        User mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setUserEmail("test@email.com");
        mockUser.setUserRole(role);

        CustomUserDetails customUserDetails = new CustomUserDetails(mockUser);
        when(customUserDetailsService.loadUserByUsername("test@email.com")).thenReturn(customUserDetails);

        return new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
    } 

    @Test
    void testGetAllUsers_AsAdmin_ShouldReturnAllUsers() throws Exception {
        Page<UserResponse> page = new PageImpl<>(List.of(createUserResponse()));
        when(userService.listAllUsers(any(Pageable.class))).thenReturn(page);

        Authentication adminAuth = createMockAuthentication(UserRole.ADMIN);

        mockMvc.perform(get("/users")
                .param("page", "0")
                .param("size", "10")
                .with(authentication(adminAuth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].userEmail").value("user@email.com"));

        verify(userService).listAllUsers(any(Pageable.class));
                
    }
        
}


