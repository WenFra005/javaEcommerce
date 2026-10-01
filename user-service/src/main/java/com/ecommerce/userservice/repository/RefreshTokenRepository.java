package com.ecommerce.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.userservice.model.RefreshToken;
import com.ecommerce.userservice.model.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findById(Long id);

    Optional<RefreshToken> findByUser(User user);

    void deleteByUser(User user);

    void deleteByToken(String token);

}
