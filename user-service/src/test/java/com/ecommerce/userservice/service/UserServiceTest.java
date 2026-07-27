package com.ecommerce.userservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ecommerce.userservice.dto.CreateAdminRequest;
import com.ecommerce.userservice.dto.CreateLegalEntityRequest;
import com.ecommerce.userservice.dto.CreateNaturalPersonRequest;
import com.ecommerce.userservice.dto.UpdateRequest;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.enums.UserRole;
import com.ecommerce.userservice.enums.UserStatus;
import com.ecommerce.userservice.enums.UserType;
import com.ecommerce.userservice.exception.EmailAlreadyExistsException;
import com.ecommerce.userservice.exception.UserNotFoundException;
import com.ecommerce.userservice.exception.ValidationException;
import com.ecommerce.userservice.model.LegalEntity;
import com.ecommerce.userservice.model.NaturalPerson;
import com.ecommerce.userservice.model.User;
import com.ecommerce.userservice.repository.LegalEntityRepository;
import com.ecommerce.userservice.repository.NaturalPersonRepository;
import com.ecommerce.userservice.repository.UserRepository;
import com.ecommerce.userservice.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NaturalPersonRepository naturalPersonRepository;

    @Mock
    private LegalEntityRepository legalEntityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User mockUserPF;
    private User mockUserPJ;
    private NaturalPerson mockNaturalPerson;
    private LegalEntity mockLegalEntity;

    @BeforeEach
    void setUpPF() {
        mockUserPF = new User();
        mockUserPF.setUserId(1L);
        mockUserPF.setName("Test User PF");
        mockUserPF.setUserEmail("test@email.com");
        mockUserPF.setUserPassword("encoded_password");
        mockUserPF.setUserStatus(UserStatus.ATIVO);
        mockUserPF.setUserRole(UserRole.CLIENTE);
        mockUserPF.setUserType(UserType.PF);

        mockNaturalPerson = new NaturalPerson();
        mockNaturalPerson.setCpf("96642170342");
        mockNaturalPerson.setBirthDate(LocalDate.of(1990, 1, 1));
        mockNaturalPerson.setUser(mockUserPF);
        mockUserPF.setNaturalPerson(mockNaturalPerson);

    }

    @BeforeEach
    void setUpPJ() {
        mockUserPJ = new User();
        mockUserPJ.setUserId(2L);
        mockUserPJ.setName("Test User PJ");
        mockUserPJ.setUserEmail("test2@email.com");
        mockUserPJ.setUserPassword("encoded_password");
        mockUserPJ.setUserStatus(UserStatus.ATIVO);
        mockUserPJ.setUserRole(UserRole.CLIENTE);
        mockUserPJ.setUserType(UserType.PJ);

        mockLegalEntity = new LegalEntity();
        mockLegalEntity.setCnpj("65944113000190");
        mockLegalEntity.setCompanyName("Test Company name");
        mockLegalEntity.setStateRegistration("256442516630");
        mockLegalEntity.setUser(mockUserPJ);
    }

    @Test
    void testCreateAdmin_WithValidData_ShouldSucceed() {
        CreateAdminRequest request = new CreateAdminRequest();
        request.setName("Admin");
        request.setEmail("admin@email.com");
        request.setPassword("password");

        when(userRepository.existsByUserEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setUserId(1L);
            return user;
        });

        UserResponse response = userService.createAdmin(request);

        assertNotNull(response);
        assertEquals("admin@email.com", response.getUserEmail());
        assertEquals(UserRole.ADMIN, response.getUserRole());
        assertEquals(UserType.SYSTEM, response.getUserType());
        verify(userRepository).save(any(User.class));

    }

    @Test
    void testCreateAdmin_WithDuplicateEmail_ShouldThrowException() {
        CreateAdminRequest request = new CreateAdminRequest();
        request.setEmail("email@example.com");

        when(userRepository.existsByUserEmail(request.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,
                () -> userService.createAdmin(request));

        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateLegalEntity_WithValidData_ShouldSucceed() {
        CreateLegalEntityRequest request = new CreateLegalEntityRequest();

        request.setName("Test User PJ");
        request.setUserEmail("test2@email.com");
        request.setUserPassword("password");
        request.setUserRole(UserRole.CLIENTE);

        request.setCnpj("65944113000190");
        request.setCompanyName("Test Company name");
        request.setStateRegistration("256442516630");

        when(userRepository.existsByUserEmail(request.getUserEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getUserPassword())).thenReturn("encoded_password");
        when(legalEntityRepository.existsByCnpj(request.getCnpj())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(mockUserPJ);

        UserResponse response = userService.createLegalEntity(request);

        assertNotNull(response);
        assertEquals("test2@email.com", response.getUserEmail());
        assertEquals(UserType.PJ, response.getUserType());
        verify(userRepository).save(any(User.class));

    }

    @Test
    void testCreateLegalEntity_WithDuplicateCnpj_ShouldThrowException() {
        CreateLegalEntityRequest request = new CreateLegalEntityRequest();

        request.setCnpj("65944113000190");

        when(legalEntityRepository.existsByCnpj(request.getCnpj())).thenReturn(true);

        assertThrows(ValidationException.class,
                () -> userService.createLegalEntity(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateLegalEntity_WithDuplicateEmail_ShouldThrowException() {
        CreateLegalEntityRequest request = new CreateLegalEntityRequest();

        request.setUserEmail("test@email.com");

        when(userRepository.existsByUserEmail(request.getUserEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,
                () -> userService.createLegalEntity(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateNaturalPerson_WithValidData_ShoundSucceed() {
        CreateNaturalPersonRequest request = new CreateNaturalPersonRequest();

        request.setName("Test User PF");
        request.setUserEmail("test@email.com");
        request.setUserPassword("password");
        request.setUserRole(UserRole.CLIENTE);
        request.setCpf("96642170342");
        request.setBirthDate(LocalDate.of(1990, 1, 1));

        when(userRepository.existsByUserEmail(request.getUserEmail())).thenReturn(false);
        when(naturalPersonRepository.existsByCpf(request.getCpf())).thenReturn(false);
        when(passwordEncoder.encode(request.getUserPassword())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(mockUserPF);

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
    void testDeleteUser_WhenOwnProfile_ShouldSucceed() {
        Long userId = 1L;
        String authenticatedEmail = "test@email.com";
        UserRole authenticatedRole = UserRole.CLIENTE;

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUserPF));

        userService.deleteUser(userId, authenticatedEmail, authenticatedRole);

        verify(userRepository).delete(mockUserPF);

    }

    @Test
    void testDeleteUser_WhenNotOwnProfile_ShouldThrowException() {
        Long userId = 1L;
        String authenticatedEmail = "other@email.com";
        UserRole authenticatedRole = UserRole.CLIENTE;

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUserPF));

        assertThrows(AccessDeniedException.class,
                () -> userService.deleteUser(userId, authenticatedEmail, authenticatedRole));
        verify(userRepository, never()).delete(any());

    }

    @Test
    void testDeleteUser_WhenUserNotFound_ShouldThrowException() {
        Long userId = 999L;
        String authenticatedEmail = "test@email.com";
        UserRole authenticatedRole = UserRole.CLIENTE;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser(userId, authenticatedEmail, authenticatedRole));
        verify(userRepository, never()).delete(any());
    }

    @Test
    void testFindUserById_WhenUserExistsAndIsOwnProfile_ShouldSucceed() {
        Long userId = 1L;
        String authenticatedEmail = "test@email.com";
        UserRole authenticatedRole = UserRole.CLIENTE;

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUserPF));

        UserResponse response = userService.findUserById(userId, authenticatedEmail, authenticatedRole);

        assertNotNull(response);
        assertEquals("test@email.com", response.getUserEmail());
    }

    @Test
    void testFindUserById_WhenUserExistsButIsNotOwnProfile_ShouldThrowException() {
        Long userId = 1L;
        String authenticatedEmail = "other@email.com";
        UserRole authenticatedRole = UserRole.CLIENTE;

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUserPF));

        assertThrows(AccessDeniedException.class,
                () -> userService.findUserById(userId, authenticatedEmail, authenticatedRole));
    }

    @Test
    void testFindUserById_WhenUserAdminAccess_ShouldSucceed() {
        Long userId = 1L;
        String authenticatedEmail = "admin@email.com";
        UserRole authenticatedRole = UserRole.ADMIN;

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUserPF));

        UserResponse response = userService.findUserById(userId, authenticatedEmail, authenticatedRole);

        assertNotNull(response);
    }

    @Test
    void testFindUserById_WhenUserNotFound_ShouldThrowException() {
        Long userId = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.findUserById(userId, "test@email.com", UserRole.CLIENTE));
    }

    @Test
    void testFindUserByEmail_WhenUserExists_ShouldSucceed() {
        String email = "test@email.com";
        UserRole role = UserRole.CLIENTE;

        when(userRepository.findByUserEmail(email)).thenReturn(Optional.of(mockUserPF));

        UserResponse response = userService.findUserByEmail(email, role);

        assertNotNull(response);
        assertEquals(email, response.getUserEmail());
    }

    @Test
    void testFindUserByEmail_WhenUserNotFound_ShouldThrowException() {
        String email = "notfound@email.com";

        when(userRepository.findByUserEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.findUserByEmail(email, UserRole.CLIENTE));

    }

    @Test
    void testListAllUsers_ShouldReturnAllUsers() {
        Pageable pageable = Pageable.unpaged();
        Page<User> userPage = new PageImpl<>(List.of(mockUserPF), pageable, 1);

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<UserResponse> responsePage = userService.listAllUsers(pageable);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        assertEquals("test@email.com", responsePage.getContent().get(0).getUserEmail());

        verify(userRepository).findAll(pageable);

    }

    @Test
    void testUpdateUser_WithValidData_ShouldSucceed() {
        Long userId = 1L;
        String authenticatedEmail = "test@email.com";
        UserRole authenticatedRole = UserRole.CLIENTE;

        UpdateRequest request = new UpdateRequest();
        request.setName("Name Updated");
        request.setUserEmail("test.updated@email.com");
        request.setCpf("90195050096");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUserPF));
        when(userRepository.findByUserEmail(request.getUserEmail())).thenReturn(Optional.empty());
        when(naturalPersonRepository.findByCpf(request.getCpf())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(mockUserPF);

        UserResponse response = userService.updateUser(userId, request, authenticatedEmail, authenticatedRole);

        assertNotNull(response);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("Name Updated", savedUser.getName());
        assertEquals("test.updated@email.com", savedUser.getUserEmail());
        assertEquals("90195050096", savedUser.getNaturalPerson().getCpf());

    }

    @Test
    void testUpdateUser_WithDuplicateEmail_ShouldThrowException() {
        Long userId = 1L;
        String authenticatedEmail = "test@email.com";
        UserRole authenticatedRole = UserRole.CLIENTE;

        UpdateRequest request = new UpdateRequest();
        request.setUserEmail("other@email.com");

        User otherUser = new User();
        otherUser.setUserId(2L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUserPF));
        when(userRepository.findByUserEmail(request.getUserEmail())).thenReturn(Optional.of(otherUser));

        assertThrows(EmailAlreadyExistsException.class,
                () -> userService.updateUser(userId, request, authenticatedEmail, authenticatedRole));

    }

    @Test
    void testUpdateUser_WithPasswordUpdate_ShouldEncodePassword() {
        Long userId = 1L;

        UpdateRequest request = new UpdateRequest();
        request.setUserPassword("new-password");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUserPF));
        when(passwordEncoder.encode("new-password")).thenReturn("encoded-new");
        when(userRepository.save(any(User.class))).thenReturn(mockUserPF);

        userService.updateUser(userId, request, "test@email.com", UserRole.CLIENTE);

        verify(passwordEncoder).encode("new-password");
        verify(userRepository).save(argThat(user -> user.getUserPassword().equals("encoded-new")));

    }

    @Test
    void testUpdateUser_WithDuplicateCpf_ShouldThrowException() {
        Long userId = 1L;
        String authenticatedEmail = "test@email.com";
        UserRole authenticatedRole = UserRole.CLIENTE;

        UpdateRequest request = new UpdateRequest();
        request.setCpf("90195050096");

        User otherUser = new User();
        otherUser.setUserId(2L);

        NaturalPerson otherPerson = new NaturalPerson();
        otherPerson.setCpf("90195050096");
        otherPerson.setUser(otherUser);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUserPF));
        when(naturalPersonRepository.findByCpf(request.getCpf())).thenReturn(Optional.of(otherPerson));

        assertThrows(ValidationException.class,
                () -> userService.updateUser(userId, request, authenticatedEmail, authenticatedRole));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateUser_WhenPFUserHasNoNaturalPerson_ShouldThrowException() {
        Long userId = 1L;
        mockUserPF.setNaturalPerson(null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUserPF));

        UpdateRequest request = new UpdateRequest();
        request.setCpf("90195050096");

        assertThrows(ValidationException.class,
                () -> userService.updateUser(userId, request, "test@email.com", UserRole.CLIENTE));

    }

    @Test
    void testUpdateUser_WhenPJUserHasNoLegalEntity_ShouldThrowException() {
        Long userId = 2L;
        mockUserPJ.setLegalEntity(null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUserPJ));

        UpdateRequest request = new UpdateRequest();
        request.setCnpj("65944113000190");

        assertThrows(ValidationException.class,
                () -> userService.updateUser(userId, request, "test2@email.com", UserRole.CLIENTE));
    }

}