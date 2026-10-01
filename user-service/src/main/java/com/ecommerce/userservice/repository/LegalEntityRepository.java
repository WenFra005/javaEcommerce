package com.ecommerce.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.userservice.model.LegalEntity;

public interface LegalEntityRepository extends JpaRepository<LegalEntity, Long> {

    Optional<LegalEntity> findByCnpj(String cnpj);

    boolean existsByCnpj(String cnpj);

}
