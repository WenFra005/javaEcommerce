package com.ecommerce.userservice.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.userservice.Model.NaturalPerson;

public interface NaturalPersonRepository extends JpaRepository<NaturalPerson, Long> {

    Optional<NaturalPerson> findByCpf(String cpf);

    boolean existsByCpf(String cpf);

}
