package com.ecommerce.userservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ecommerce.userservice.config.SecurityConfig;
import com.ecommerce.userservice.dto.UpdateRequest;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.enums.UserRole;
import com.ecommerce.userservice.enums.UserType;
import com.ecommerce.userservice.exception.EmailAlreadyExistsException;
import com.ecommerce.userservice.exception.UserNotFoundException;
import com.ecommerce.userservice.exception.ValidationException;
import com.ecommerce.userservice.model.User;
import com.ecommerce.userservice.security.CustomUserDetails;
import com.ecommerce.userservice.security.JwtUtil;
import com.ecommerce.userservice.service.CustomUserDetailsService;
import com.ecommerce.userservice.service.UserService;

@WebMvcTest(controllers = UserController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class UserControllerTest {

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

                return new UsernamePasswordAuthenticationToken(customUserDetails, null,
                                customUserDetails.getAuthorities());
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

        @Test
        void testGetAllUsers_AsCliente_ShouldReturnForbidden() throws Exception {
                Authentication clienteAuth = createMockAuthentication(UserRole.CLIENTE);

                mockMvc.perform(get("/users")
                                .with(authentication(clienteAuth)))
                                .andExpect(status().isForbidden());

                verify(userService, never()).listAllUsers(any());
        }

        @Test
        void testGetAllUsers_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
                mockMvc.perform(get("/users"))
                                .andExpect(status().isUnauthorized());

                verify(userService, never()).listAllUsers(any());
        }

        @Test
        void testGetUser_OwnProfile_ShouldReturnUser() throws Exception {
                Long userId = 1L;
                UserResponse userResponse = createUserResponse();

                when(userService.findUserById(eq(userId), anyString(), any(UserRole.class))).thenReturn(userResponse);

                Authentication clienteAuth = createMockAuthentication(UserRole.CLIENTE);

                mockMvc.perform(get("/users/{id}", userId)
                                .with(authentication(clienteAuth)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.userEmail").value("user@email.com"));

                verify(userService).findUserById(eq(userId), anyString(), any(UserRole.class));

        }

        @Test
        void testGetUser_AsAdmin_ShouldReturnUser() throws Exception {
                Long userId = 2L;
                UserResponse userResponse = createUserResponse();

                when(userService.findUserById(eq(userId), anyString(), eq(UserRole.ADMIN))).thenReturn(userResponse);

                Authentication adminAuth = createMockAuthentication(UserRole.ADMIN);

                mockMvc.perform(get("/users/{id}", userId)
                                .with(authentication(adminAuth)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.userEmail").value("user@email.com"));

                verify(userService).findUserById(eq(userId), anyString(), eq(UserRole.ADMIN));
        }

        @Test
        void testGetUser_OtherUser_ShouldReturnForbidden() throws Exception {
                Long userId = 2L;

                when(userService.findUserById(eq(userId), anyString(), any(UserRole.class)))
                                .thenThrow(new AccessDeniedException("Acesso negado"));

                Authentication clienteAuth = createMockAuthentication(UserRole.CLIENTE);

                mockMvc.perform(get("/users/{id}", userId)
                                .with(authentication(clienteAuth)))
                                .andExpect(status().isForbidden())
                                .andExpect(jsonPath("$.message").value("Acesso negado"));

                verify(userService).findUserById(eq(userId), anyString(), any(UserRole.class));
        }

        @Test
        void testGetUser_NotFound_ShouldReturnNotFound() throws Exception {
                Long userId = 999L;

                when(userService.findUserById(eq(userId), anyString(), any(UserRole.class)))
                                .thenThrow(new UserNotFoundException("Usuário não encontrado"));

                Authentication clienteAuth = createMockAuthentication(UserRole.CLIENTE);

                mockMvc.perform(get("/users/{id}", userId)
                                .with(authentication(clienteAuth)))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Usuário não encontrado"));

                verify(userService).findUserById(eq(userId), anyString(), any(UserRole.class));
        }

        @Test
        void testGetUser_InvalidToken_ShouldReturnUnauthorized() throws Exception {
                Long userId = 1L;

                mockMvc.perform(get("/users/{id}", userId))
                                .andExpect(status().isUnauthorized());

                verify(userService, never()).findUserById(eq(userId), anyString(), any(UserRole.class));
        }

        @Test
        void testDeleteUser_OwnProfile_ShouldReturnNoContent() throws Exception {
                Long userId = 1L;

                Authentication auth = createMockAuthentication(UserRole.CLIENTE);

                mockMvc.perform(delete("/users/delete/{id}", userId)
                                .with(authentication(auth)))
                                .andExpect(status().isNoContent());

                verify(userService).deleteUser(eq(userId), anyString(), any(UserRole.class));

        }

        @Test
        void testDeleteUser_AsAdmin_ShouldReturnNoContent() throws Exception {
                Long userId = 1L;

                Authentication auth = createMockAuthentication(UserRole.ADMIN);

                mockMvc.perform(delete("/users/delete/{id}", userId)
                                .with(authentication(auth)))
                                .andExpect(status().isNoContent());

                verify(userService).deleteUser(eq(userId), anyString(), any(UserRole.class));

        }

        @Test
        void testDeleteUser_OtherUser_ShouldReturnForbidden() throws Exception {
                Long userId = 2L;

                doThrow(new AccessDeniedException("Acesso negado"))
                                .when(userService).deleteUser(eq(userId), anyString(), any(UserRole.class));

                Authentication auth = createMockAuthentication(UserRole.CLIENTE);

                mockMvc.perform(delete("/users/delete/{id}", userId)
                                .with(authentication(auth)))
                                .andExpect(status().isForbidden());

                verify(userService).deleteUser(eq(userId), anyString(), any(UserRole.class));

        }

        @Test
        void testDeleteUser_NotFound_ShouldReturnNotFound() throws Exception {
                Long userId = 999L;

                doThrow(new UserNotFoundException("Usuário não encontrado"))
                                .when(userService).deleteUser(eq(userId), anyString(), any(UserRole.class));

                Authentication auth = createMockAuthentication(UserRole.CLIENTE);

                mockMvc.perform(delete("/users/delete/{id}", userId)
                                .with(authentication(auth)))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Usuário não encontrado"));

                verify(userService).deleteUser(eq(userId), anyString(), any(UserRole.class));
        }

        @Test
        void testDeleteUser_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
                Long userId = 1L;

                mockMvc.perform(delete("/users/delete/{id}", userId))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void testGetCurrentUser_WithValidAuth_ShouldReturnProfile() throws Exception {
                UserResponse userResponse = createUserResponse();

                when(userService.findUserByEmail(anyString()))
                                .thenReturn(userResponse);

                Authentication clienteAuth = createMockAuthentication(UserRole.CLIENTE);

                mockMvc.perform(get("/users/me")
                                .with(authentication(clienteAuth)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.userEmail").value("user@email.com"));
        }

        @Test
        void testGetCurrentUser_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
                mockMvc.perform(get("/users/me"))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void testPutUser_OwnProfile_ShouldReturnUpdatedProfileWithToken() throws Exception {
                Long userId = 1L;
                UserResponse updatedUserResponse = createUserResponse();
                updatedUserResponse.setUserEmail("updated@email.com");

                when(userService.updateUser(eq(userId), any(UpdateRequest.class), anyString(), any(UserRole.class)))
                                .thenReturn(updatedUserResponse);
                when(jwtUtil.generateToken(anyString(), anyLong()))
                                .thenReturn("new-token");

                Authentication auth = createMockAuthentication(UserRole.CLIENTE);

                String requestJson = """
                                {
                                    "name": "Updated Name",
                                    "userEmail": "updated@email.com"
                                }
                                """;

                mockMvc.perform(put("/users/update/{id}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                                .with(authentication(auth)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.userResponse.userEmail").value("updated@email.com"))
                                .andExpect(jsonPath("$.token").value("new-token"));

                verify(userService).updateUser(eq(userId), any(UpdateRequest.class), anyString(), any(UserRole.class));
                verify(jwtUtil).generateToken(anyString(), anyLong());

        }

        @Test
        void testPutUser_AsAdmin_ShouldReturnSucced() throws Exception {
                Long userId = 2L;
                UserResponse updatedUserResponse = createUserResponse();
                updatedUserResponse.setUserEmail("updated@email.com");

                when(userService.updateUser(eq(userId), any(UpdateRequest.class), anyString(), eq(UserRole.ADMIN)))
                                .thenReturn(updatedUserResponse);
                when(jwtUtil.generateToken(anyString(), anyLong()))
                                .thenReturn("new-token");

                Authentication auth = createMockAuthentication(UserRole.ADMIN);

                String requestJson = """
                                {
                                    "name": "Updated Name",
                                    "userEmail": "updated@email.com"
                                }
                                """;

                mockMvc.perform(put("/users/update/{id}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                                .with(authentication(auth)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.userResponse.userEmail").value("updated@email.com"))
                                .andExpect(jsonPath("$.token").value("new-token"));

                verify(userService).updateUser(eq(userId), any(UpdateRequest.class), anyString(), eq(UserRole.ADMIN));
                verify(jwtUtil).generateToken(anyString(), anyLong());
        }

        @Test
        void testPutUser_OtherUser_ShouldReturnForbidden() throws Exception {
                Long userId = 2L;

                when(userService.updateUser(eq(userId), any(UpdateRequest.class), anyString(), any(UserRole.class)))
                                .thenThrow(new AccessDeniedException("Acesso negado"));

                Authentication auth = createMockAuthentication(UserRole.CLIENTE);

                String requestJson = """
                                {
                                    "name": "Updated Name",
                                    "userEmail": "updated@email.com"
                                }
                                """;

                mockMvc.perform(put("/users/update/{id}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                                .with(authentication(auth)))
                                .andExpect(status().isForbidden());

                verify(userService).updateUser(eq(userId), any(UpdateRequest.class), anyString(), any(UserRole.class));
        }

        @Test
        void testPutUser_DuplicateEmail_ShouldReturnConflict() throws Exception {
                Long userId = 1L;

                when(userService.updateUser(eq(userId), any(UpdateRequest.class), anyString(), any(UserRole.class)))
                                .thenThrow(new EmailAlreadyExistsException("E-mail já cadastrado"));

                Authentication auth = createMockAuthentication(UserRole.CLIENTE);

                String requestJson = """
                                {
                                    "userEmail": "existing@email.com"
                                }
                                """;

                mockMvc.perform(put("/users/update/{id}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                                .with(authentication(auth)))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.message").value("E-mail já cadastrado"));

                verify(userService).updateUser(eq(userId), any(UpdateRequest.class), anyString(), any(UserRole.class));
        }

        @Test
        void testPutUser_InvalidCpf_ShouldReturnBadRequest() throws Exception {
                Long userId = 1L;

                when(userService.updateUser(eq(userId), any(UpdateRequest.class), anyString(), any(UserRole.class)))
                                .thenThrow(new ValidationException("CPF inválido"));

                Authentication auth = createMockAuthentication(UserRole.CLIENTE);

                String requestJson = """
                                {
                                    "cpf": "12345678900"
                                }
                                """;

                mockMvc.perform(put("/users/update/{id}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                                .with(authentication(auth)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("cpf: CPF should be valid"));
        }

        @Test
        void testPutUser_NotFound_ShouldReturnNotFound() throws Exception {
                Long userId = 999L;

                when(userService.updateUser(eq(userId), any(UpdateRequest.class), anyString(), any(UserRole.class)))
                                .thenThrow(new UserNotFoundException("Usuário não encontrado"));

                Authentication auth = createMockAuthentication(UserRole.CLIENTE);

                String requestJson = """
                                {
                                    "name": "Updated Name"
                                }
                                """;

                mockMvc.perform(put("/users/update/{id}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                                .with(authentication(auth)))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Usuário não encontrado"));

                verify(userService).updateUser(eq(userId), any(UpdateRequest.class), anyString(), any(UserRole.class));
        }

        @Test
        void testPutUser_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
                Long userId = 1L;

                String requestJson = """
                                {
                                    "name": "Updated Name"
                                }
                                """;

                mockMvc.perform(put("/users/update/{id}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                                .andExpect(status().isUnauthorized());

                verify(userService, never()).updateUser(eq(userId), any(UpdateRequest.class), anyString(),
                                any(UserRole.class));
        }

}