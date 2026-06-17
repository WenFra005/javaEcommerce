package com.ecommerce.userservice.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.userservice.Model.User;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByUserEmail(String userEmail);
}
