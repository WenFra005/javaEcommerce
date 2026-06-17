package com.ecommerce.userservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.userservice.Model.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
