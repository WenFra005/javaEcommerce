package com.ecommerce.userservice.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class AdminSeederTest {

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
    void testRun_WhenAdminDoesNotExist_ShouldCreateAdmin() throws Exception {

        when(userRepository.existsByUserRole(UserRole.ADMIN)).thenReturn(false);
        when(passwordEncoder.encode("admin-password")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        adminSeeder.run();

        verify(userRepository).save(argThat(user -> user.getUserEmail().equals("admin@email.com") &&
                user.getUserRole().equals(UserRole.ADMIN) &&
                user.getUserStatus().equals(UserStatus.ATIVO) &&
                user.getUserType().equals(UserType.SYSTEM)));

    }

    @Test
    void testRun_WhenAdminAlreadyExists_ShouldNotCreateAdmin() throws Exception {
        when(userRepository.existsByUserRole(UserRole.ADMIN)).thenReturn(true);

        adminSeeder.run();

        verify(userRepository, never()).save(any());
    }

    @Test
    void testRun_WhenSeedingDisabled_ShouldNotCreateAdmin() throws Exception {
        ReflectionTestUtils.setField(adminSeeder, "seedingEnabled", false);

        adminSeeder.run();

        verify(userRepository, never()).existsByUserRole(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRun_WhenEmailNotSet_ShouldNotCreateAdmin() throws Exception {
        ReflectionTestUtils.setField(adminSeeder, "adminEmail", null);

        when(userRepository.existsByUserRole(UserRole.ADMIN)).thenReturn(false);

        adminSeeder.run();

        verify(userRepository, never()).save(any());
    }

    @Test
    void testRun_WhenPasswordNotSet_ShouldNotCreateAdmin() throws Exception {
        ReflectionTestUtils.setField(adminSeeder, "adminPassword", null);

        when(userRepository.existsByUserRole(UserRole.ADMIN)).thenReturn(false);

        adminSeeder.run();

        verify(userRepository, never()).save(any());
    }
}
