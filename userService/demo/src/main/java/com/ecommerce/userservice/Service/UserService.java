package com.ecommerce.userservice.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.userservice.Model.User;
import com.ecommerce.userservice.Repository.UserRepository;
import com.ecommerce.userservice.dto.CreateUserRequest;
import com.ecommerce.userservice.dto.UserResponse;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse createUser(CreateUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setUserEmail(request.getUserEmail());
        user.setUserPassword(request.getUserPassword());

        User savedUser = userRepository.save(user);
        return toUserResponse(savedUser);
    }

    public UserResponse findUserById(Long id) {
        User user = userRepository.findById(id).orElse(null); 
        if (user == null) {
            return null;
        }

        return toUserResponse(user);
    }

    public UserResponse updateUser(Long id, CreateUserRequest updatedUser) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        user.setName(updatedUser.getName());
        user.setUserEmail(updatedUser.getUserEmail());
        user.setUserPassword(updatedUser.getUserPassword());

        User savedUser = userRepository.save(user);
        return toUserResponse(savedUser);

    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void updateStatus(Long id, String status) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return;
        }
        user.setUserStatus(null);
        userRepository.save(user);
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
            user.getUserId(),
            user.getName(),
            user.getUserEmail(),
            user.getUserStatus() != null ? user.getUserStatus().name() : null,
            user.getUserCreatedAt()
        );
    }


}
