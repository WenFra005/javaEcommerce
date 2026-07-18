package com.ecommerce.userservice.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ecommerce.userservice.Service.UserService;
import com.ecommerce.userservice.dto.UserResponse;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void testDeleteUser() {

    }

    @Test
    void testGetCurrentUser() {

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetListAll_WithRoleAdmin_ShouldReturnAllUsers() {

    }

    @Test
    void testGetUser() {

    }

    @Test
    void testPutUser() {

    }
}
