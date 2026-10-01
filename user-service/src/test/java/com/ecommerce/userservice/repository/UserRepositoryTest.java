package com.ecommerce.userservice.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import com.ecommerce.userservice.config.PostgresqlContainerConfig;
import com.ecommerce.userservice.enums.UserRole;
import com.ecommerce.userservice.enums.UserStatus;
import com.ecommerce.userservice.model.User;
import com.ecommerce.userservice.util.TestDataFactory;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PostgresqlContainerConfig.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testExistsByUserEmail() {
        User user = TestDataFactory.createUser("test@email.com");

        boolean existsBeforeSave = userRepository.existsByUserEmail("test@email.com");

        userRepository.save(user);

        boolean existsAfterSave = userRepository.existsByUserEmail("test@email.com");
        boolean notExists = userRepository.existsByUserEmail("nonexistent@email.com");

        assertThat(existsBeforeSave).isFalse();
        assertThat(existsAfterSave).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void testExistsByUserEmail_WhenUserDoesNotExist() {
        boolean exists = userRepository.existsByUserEmail("nonexistent@email.com");

        assertThat(exists).isFalse();
    }

    @Test
    void testExistsByUserRole() {
        User user = TestDataFactory.createUser("test@email.com");
        user.setUserRole(UserRole.ADMIN);

        userRepository.save(user);

        boolean exists = userRepository.existsByUserRole(UserRole.ADMIN);
        boolean notExists = userRepository.existsByUserRole(UserRole.CLIENTE);

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void testFindByUserEmail() {
        User user = TestDataFactory.createUser("test@email.com");

        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findByUserEmail("test@email.com");
        Optional<User> notFoundUser = userRepository.findByUserEmail("nonexistent@email.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUserId()).isEqualTo(savedUser.getUserId());
        assertThat(foundUser.get().getUserEmail()).isEqualTo("test@email.com");
        assertThat(notFoundUser).isNotPresent();
    }

    @Test
    void testFindByUserEmailAndUserStatus() {
        User activeUser = TestDataFactory.createUserWithStatus("active@email.com", UserStatus.ATIVO);
        User inactiveUser = TestDataFactory.createUserWithStatus("inactive@email.com", UserStatus.INATIVO);

        userRepository.save(activeUser);
        userRepository.save(inactiveUser);

        Optional<User> foundActiveUser = userRepository.findByUserEmailAndUserStatus("active@email.com",
                UserStatus.ATIVO);
        Optional<User> foundInactiveUser = userRepository.findByUserEmailAndUserStatus("inactive@email.com",
                UserStatus.INATIVO);
        Optional<User> notFoundUser = userRepository.findByUserEmailAndUserStatus("nonexistent@email.com",
                UserStatus.ATIVO);

        assertThat(foundActiveUser).isPresent();
        assertThat(foundInactiveUser).isPresent();
        assertThat(notFoundUser).isNotPresent();

    }

    @Test
    void testFindByUserRole() {
        User adminUser = TestDataFactory.createAdmin("admin@email.com");
        User clienteUser = TestDataFactory.createUser("cliente@email.com");

        userRepository.save(adminUser);
        userRepository.save(clienteUser);

        List<User> foundAdmin = userRepository.findByUserRole(UserRole.ADMIN);
        List<User> foundClients = userRepository.findByUserRole(UserRole.CLIENTE);
        List<User> notFoundUsers = userRepository.findByUserRole(UserRole.FORNECEDOR);

        assertThat(foundAdmin).hasSize(1);
        assertThat(foundAdmin.get(0).getUserEmail()).isEqualTo("admin@email.com");
        assertThat(foundClients).hasSize(1);
        assertThat(foundClients.get(0).getUserEmail()).isEqualTo("cliente@email.com");
        assertThat(notFoundUsers).isEmpty();
    }
}
