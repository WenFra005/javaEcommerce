package com.ecommerce.userservice.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.ecommerce.userservice.enums.UserRole;
import com.ecommerce.userservice.enums.UserStatus;
import com.ecommerce.userservice.enums.UserType;
import com.ecommerce.userservice.model.User;
import com.ecommerce.userservice.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AdminSeederTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminSeeder adminSeeder;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(adminSeeder, "seedingEnabled", true);
        ReflectionTestUtils.setField(adminSeeder, "adminEmail", "admin@email.com");
        ReflectionTestUtils.setField(adminSeeder, "adminPassword", "admin-password");

    }

    @Test
    void testRunWhenAdminDoesNotExistShouldCreateAdmin() throws Exception {

        when(userRepository.existsByUserRole(UserRole.ADMIN)).thenReturn(false);
        when(passwordEncoder.encode("admin-password")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow((Executable) adminSeeder::run);

        verify(userRepository).save(argThat(user -> user.getUserEmail().equals("admin@email.com") &&
                user.getUserRole().equals(UserRole.ADMIN) &&
                user.getUserStatus().equals(UserStatus.ATIVO) &&
                user.getUserType().equals(UserType.SYSTEM)));

    }

    @Test
    void testRunWhenAdminAlreadyExistsShouldNotCreateAdmin() throws Exception {
        when(userRepository.existsByUserRole(UserRole.ADMIN)).thenReturn(true);

        assertDoesNotThrow((Executable) adminSeeder::run);

        verify(userRepository, never()).save(any());
    }

    @Test
    void testRunWhenSeedingDisabledShouldNotCreateAdmin() throws Exception {
        ReflectionTestUtils.setField(adminSeeder, "seedingEnabled", false);

        assertDoesNotThrow((Executable) adminSeeder::run);

        verify(userRepository, never()).existsByUserRole(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRunWhenEmailNotSetShouldNotCreateAdmin() throws Exception {
        ReflectionTestUtils.setField(adminSeeder, "adminEmail", null);

        when(userRepository.existsByUserRole(UserRole.ADMIN)).thenReturn(false);

        assertDoesNotThrow((Executable) adminSeeder::run);

        verify(userRepository, never()).save(any());
    }

    @Test
    void testRunWhenPasswordNotSetShouldNotCreateAdmin() throws Exception {
        ReflectionTestUtils.setField(adminSeeder, "adminPassword", null);

        when(userRepository.existsByUserRole(UserRole.ADMIN)).thenReturn(false);

        assertDoesNotThrow((Executable) adminSeeder::run);

        verify(userRepository, never()).save(any());
    }
}
