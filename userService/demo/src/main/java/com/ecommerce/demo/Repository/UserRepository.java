package com.ecommerce.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.demo.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
