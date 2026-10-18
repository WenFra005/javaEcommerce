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
import com.ecommerce.userservice.model.NaturalPerson;
import com.ecommerce.userservice.model.User;
import com.ecommerce.userservice.util.TestDataFactory;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PostgresqlContainerConfig.class)
class NaturalPersonRepositoryTest {

    @Autowired
    private NaturalPersonRepository naturalPersonRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testExistsByCpf() {
        User user = userRepository.save(TestDataFactory.createUser("pf@email.com"));
        NaturalPerson np = TestDataFactory.createNaturalPerson(user, "52512984044");
        naturalPersonRepository.save(np);

        boolean exists = naturalPersonRepository.existsByCpf("52512984044");
        boolean notExists = naturalPersonRepository.existsByCpf("12345678900");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();

    }

    @Test
    void testFindByCpf() {
        User user = userRepository.save(TestDataFactory.createUser("pf@email.com"));
        NaturalPerson np = TestDataFactory.createNaturalPerson(user, "52512984044");
        naturalPersonRepository.save(np);

        Optional<NaturalPerson> found = naturalPersonRepository.findByCpf("52512984044");
        Optional<NaturalPerson> notFound = naturalPersonRepository.findByCpf("12345678900");

        assertThat(found).isPresent();
        assertThat(found.get().getUser().getUserEmail()).isEqualTo("pf@email.com");
        assertThat(found.get().getCpf()).isEqualTo("52512984044");
        assertThat(notFound).isNotPresent();
    }
}
