package com.ecommerce.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.userservice.model.RefreshToken;
import com.ecommerce.userservice.model.User;

import jakarta.persistence.LockModeType;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select refreshToken from refresh_tokens refreshToken where refreshToken.token = :token")
    Optional<RefreshToken> findByTokenForUpdate(@Param("token") String token);

    Optional<RefreshToken> findById(Long id);

    Optional<RefreshToken> findByUser(User user);

    void deleteByUser(User user);

    void deleteByToken(String token);

}
