package com.ecommerce.userservice.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.userservice.Enums.UserStatus;
import com.ecommerce.userservice.Exception.UserNotFoundException;
import com.ecommerce.userservice.Model.User;
import com.ecommerce.userservice.Repository.UserRepository;
import com.ecommerce.userservice.dto.CreateUserRequest;
import com.ecommerce.userservice.dto.UserResponse;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(CreateUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setUserEmail(request.getUserEmail());
        user.setUserPassword(passwordEncoder.encode(request.getUserPassword()));
        user.setUserStatus(UserStatus.ATIVO);

        User savedUser = userRepository.save(user);
        return toUserResponse(savedUser);
    }

    public UserResponse findUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado para o ID: " + id));
        
        return toUserResponse(user);
       
    }

    public UserResponse updateUser(Long id, CreateUserRequest updatedUser) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado para atualização: " + id));
        user.setName(updatedUser.getName());
        user.setUserEmail(updatedUser.getUserEmail());
        user.setUserPassword(passwordEncoder.encode(updatedUser.getUserPassword()));

        User savedUser = userRepository.save(user);
        return toUserResponse(savedUser);

    }

    public void updateStatus(Long id, String status) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado para atualização de status: " + id));
        user.setUserStatus(null);
        userRepository.save(user);
    }


    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuário não encontrado para exclusão: " + id);
        }

        userRepository.deleteById(id);
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
