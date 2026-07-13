package com.ecommerce.userservice.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.userservice.Enums.UserStatus;
import com.ecommerce.userservice.Model.User;
import com.ecommerce.userservice.Enums.UserRole;


public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByUserEmail(String userEmail);

    Optional<User> findByUserEmailAndUserStatus(String userEmail, UserStatus userStatus);

    List<User> findByUserRole(UserRole userRole);
    
    boolean existsByUserEmail(String userEmail);

    boolean existsByUserRole(UserRole userRole);
}
