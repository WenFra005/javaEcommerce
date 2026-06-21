package com.ecommerce.userservice.Service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.userservice.Enums.UserStatus;
import com.ecommerce.userservice.Exception.EmailAlreadyExistsException;
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
        if (userRepository.findByUserEmail(request.getUserEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists: " + request.getUserEmail());
        }

        User user = new User();
        user.setName(request.getName());
        user.setUserEmail(request.getUserEmail());
        user.setUserPassword(passwordEncoder.encode(request.getUserPassword()));
        user.setUserStatus(UserStatus.ATIVO);

        User savedUser = userRepository.save(user);
        return toUserResponse(savedUser);
    }

    public UserResponse findUserById(Long id, String authenticatedUserEmail) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado para o ID: " + id));
        
        if (!user.getUserEmail().equals(authenticatedUserEmail)) {
            throw new AccessDeniedException("Usuário não autorizado para acessar este usuário");
        }

        return toUserResponse(user);
       
    }

    public UserResponse updateUser(Long id, CreateUserRequest request, String authenticatedUserEmail) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado para atualização: " + id));

        if (!user.getUserEmail().equals(authenticatedUserEmail)) {
            throw new AccessDeniedException("Usuário não autorizado para atualizar este usuário");
        }

        if (request.getUserEmail() != null && !request.getUserEmail().equals(user.getUserEmail())) {
            userRepository.findByUserEmail(request.getUserEmail())
                .ifPresent(existingUser -> {
                    if (!existingUser.getUserId().equals(id)) {
                        throw new EmailAlreadyExistsException("Email already exists: " + request.getUserEmail());
                    }
                });
        }

        user.setName(request.getName());
        user.setUserEmail(request.getUserEmail());
        user.setUserPassword(passwordEncoder.encode(request.getUserPassword()));

        User savedUser = userRepository.save(user);
        return toUserResponse(savedUser);

    }

    public UserResponse updateStatus(Long id, CreateUserRequest request, String authenticatedUserEmail) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado para atualização de status: " + id));
        
        if (!user.getUserEmail().equals(authenticatedUserEmail)) {
            throw new AccessDeniedException("Usuário não autorizado para atualizar o status deste usuário");
        }

        user.setUserStatus(null);
        userRepository.save(user);
        return toUserResponse(user);
    }


    public void deleteUser(Long id, String authenticatedUserEmail) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não econtrado"));

        if (!user.getUserEmail().equals(authenticatedUserEmail)) {
            throw new AccessDeniedException("Você não tem permissão para deletar este usuário");
        }
        userRepository.delete(user);
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
