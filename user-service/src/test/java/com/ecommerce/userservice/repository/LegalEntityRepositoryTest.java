package com.ecommerce.userservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.ecommerce.userservice.config.PostgresqlContainerConfig;
import com.ecommerce.userservice.model.LegalEntity;
import com.ecommerce.userservice.model.User;
import com.ecommerce.userservice.util.TestDataFactory;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PostgresqlContainerConfig.class)
class LegalEntityRepositoryTest {

    @Autowired
    private LegalEntityRepository legalEntityRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testExistsByCnpj() {
        User user = userRepository.save(TestDataFactory.createUser("pf@email.com"));
        LegalEntity le = TestDataFactory.createLegalEntity(user, "93703740000175", "Test Company", "593575235491");
        legalEntityRepository.save(le);

        boolean exists = legalEntityRepository.existsByCnpj("93703740000175");
        boolean notExists = legalEntityRepository.existsByCnpj("12345678901234");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();

    }

    @Test
    void testFindByCnpj() {
        User user = userRepository.save(TestDataFactory.createUser("pf@email.com"));
        LegalEntity le = TestDataFactory.createLegalEntity(user, "93703740000175", "Test Company", "593575235491");
        legalEntityRepository.save(le);

        Optional<LegalEntity> found = legalEntityRepository.findByCnpj("93703740000175");
        Optional<LegalEntity> notFound = legalEntityRepository.findByCnpj("12345678900");

        assertThat(found).isPresent();
        assertThat(found.get().getUser().getUserEmail()).isEqualTo("pf@email.com");
        assertThat(found.get().getCnpj()).isEqualTo("93703740000175");
        assertThat(found.get().getCompanyName()).isEqualTo("Test Company");
        assertThat(found.get().getStateRegistration()).isEqualTo("593575235491");
        assertThat(notFound).isNotPresent();
    }
}
