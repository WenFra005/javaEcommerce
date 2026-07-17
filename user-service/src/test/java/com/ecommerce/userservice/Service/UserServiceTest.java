package com.ecommerce.userservice.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ecommerce.userservice.Enums.UserRole;
import com.ecommerce.userservice.Enums.UserStatus;
import com.ecommerce.userservice.Enums.UserType;
import com.ecommerce.userservice.Exception.EmailAlreadyExistsException;
import com.ecommerce.userservice.Exception.UserNotFoundException;
import com.ecommerce.userservice.Exception.ValidationException;
import com.ecommerce.userservice.Model.LegalEntity;
import com.ecommerce.userservice.Model.NaturalPerson;
import com.ecommerce.userservice.Model.User;
import com.ecommerce.userservice.Repository.LegalEntityRepository;
import com.ecommerce.userservice.Repository.NaturalPersonRepository;
import com.ecommerce.userservice.Repository.UserRepository;
import com.ecommerce.userservice.dto.CreateNaturalPersonRequest;
import com.ecommerce.userservice.dto.UserResponse;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock private UserRepository userRepository;
    @Mock private NaturalPersonRepository naturalPersonRepository;
    @Mock private LegalEntityRepository legalEntityRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User mockUser;
    private NaturalPerson mockNaturalPerson;
    private LegalEntity mockLegalEntity;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setName("Test User");
        mockUser.setUserEmail("test@email.com");
        mockUser.setUserPassword("encoded_password");
        mockUser.setUserStatus(UserStatus.ATIVO);
        mockUser.setUserRole(UserRole.CLIENTE);
        mockUser.setUserType(UserType.PF);

        mockNaturalPerson = new NaturalPerson();
        mockNaturalPerson.setCpf("96642170342");
        mockNaturalPerson.setBirthDate(LocalDate.of(1990, 1, 1));
        mockNaturalPerson.setUser(mockUser);

        mockLegalEntity = new LegalEntity();
        mockLegalEntity.setCnpj("65944113000190");
        mockLegalEntity.setCompanyName("Test Company name");
        mockLegalEntity.setStateRegistration("256442516630");
        mockLegalEntity.setUser(mockUser);
    }
    
    @Test
    void testCreateAdmin() {

    }

    @Test
    void testCreateLegalEntity() {

    }

    @Test
    void testCreateNaturalPerson_WithValidData_ShoundSucceed() {
        CreateNaturalPersonRequest request = new CreateNaturalPersonRequest();

        request.setName("Test User");
        request.setUserEmail("test@email.com");
        request.setUserPassword("password");
        request.setUserRole(UserRole.CLIENTE);
        request.setCpf("96642170342");
        request.setBirthDate(LocalDate.of(1990, 1, 1));

        when(userRepository.existsByUserEmail(request.getUserEmail())).thenReturn(false);
        when(naturalPersonRepository.existsByCpf(request.getCpf())).thenReturn(false);
        when(passwordEncoder.encode(request.getUserPassword())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        UserResponse response = userService.createNaturalPerson(request);

        assertNotNull(response);
        assertEquals("test@email.com", response.getUserEmail());
        assertEquals(UserType.PF, response.getUserType());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateNatural_PersonWithDuplicateCpf_ShouldTrownException() {
        CreateNaturalPersonRequest request = new CreateNaturalPersonRequest();

        request.setUserEmail("test@email.com");
        request.setCpf("96642170342");

        when(naturalPersonRepository.existsByCpf(request.getCpf())).thenReturn(true);


        assertThrows(ValidationException.class, 
            () -> userService.createNaturalPerson(request));
        verify(userRepository, never()).save(any());

    }

    @Test
    void testCreateNaturalPerson_WithDuplicateEmail_ShouldThrowException() {
        CreateNaturalPersonRequest request = new CreateNaturalPersonRequest();

        request.setUserEmail("test@email.com");

        when(userRepository.existsByUserEmail(request.getUserEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, 
            () -> userService.createNaturalPerson(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testDeleteUser() {

    }

    @Test
    void testFindUserById_WhenUserExistsAndIsOwnProfile_ShouldSucceed() {
        Long userId = 1L;
        String authenticatedEmail = "test@email.com";
        UserRole authenticatedRole = UserRole.CLIENTE;

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        UserResponse response = userService.findUserById(userId, authenticatedEmail, authenticatedRole);

        assertNotNull(response);
        assertEquals("test@email.com", response.getUserEmail());
    }

    @Test
    void testFindUserById_WhenUserExistsButIsNotOwnProfile_ShouldThrowException() {
        Long userId = 1L;
        String authenticatedEmail = "other@email.com";
        UserRole authenticatedRole = UserRole.CLIENTE;

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        assertThrows(AccessDeniedException.class, 
            () -> userService.findUserById(userId, authenticatedEmail, authenticatedRole)
        );
    }

    @Test
    void testFindUserById_WhenUserAdminAccess_ShouldSucceed() {
        Long userId = 1L;
        String authenticatedEmail = "admin@email.com";
        UserRole authenticatedRole = UserRole.ADMIN;

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
    
        UserResponse response = userService.findUserById(userId, authenticatedEmail, authenticatedRole);

        assertNotNull(response);
    }

    @Test
    void testFindUserById_WhenUserNotFound_ShouldThrowException() {
        Long userId = 999L;
        
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, 
            () -> userService.findUserById(userId, "test@email.com", UserRole.CLIENTE)
        );
    }

    @Test
    void testListAllUsers() {

    }

    @Test
    void testUpdateUser_WithValidData_ShouldSucceed() {

    }
}
