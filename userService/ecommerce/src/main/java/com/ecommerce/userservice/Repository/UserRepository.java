package com.ecommerce.userservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.userservice.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
