package com.ecommerce.userservice.Service;

import java.util.function.Consumer;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.userservice.Enums.UserRole;
import com.ecommerce.userservice.Enums.UserStatus;
import com.ecommerce.userservice.Enums.UserType;
import com.ecommerce.userservice.Exception.EmailAlreadyExistsException;
import com.ecommerce.userservice.Exception.UserNotFoundException;
import com.ecommerce.userservice.Model.LegalEntity;
import com.ecommerce.userservice.Model.NaturalPerson;
import com.ecommerce.userservice.Model.User;
import com.ecommerce.userservice.Repository.LegalEntityRepository;
import com.ecommerce.userservice.Repository.NaturalPersonRepository;
import com.ecommerce.userservice.Repository.UserRepository;
import com.ecommerce.userservice.dto.CreateLegalEntityRequest;
import com.ecommerce.userservice.dto.CreateNaturalPersonRequest;
import com.ecommerce.userservice.dto.CreateUserRequest;
import com.ecommerce.userservice.dto.UpdateRequest;
import com.ecommerce.userservice.dto.UserResponse;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final NaturalPersonRepository naturalPersonRepository;
    private final LegalEntityRepository legalEntityRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            NaturalPersonRepository naturalPersonRepository, LegalEntityRepository legalEntityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.naturalPersonRepository = naturalPersonRepository;
        this.legalEntityRepository = legalEntityRepository;
    }

    @Transactional
    public UserResponse createNaturalPerson(CreateNaturalPersonRequest request) {
        if (naturalPersonRepository.existsByCpf(request.getCpf())) {
            throw new ValidationException("CPF already exists: " + request.getCpf());
        }

        return createUserWithType(request, UserType.PF, user -> {
            NaturalPerson naturalPerson = builderNaturalPerson(request, user);
            user.setNaturalPerson(naturalPerson);
        });
    }

    @Transactional
    public UserResponse createLegalEntity(CreateLegalEntityRequest request) {
        if (legalEntityRepository.existsByCnpj(request.getCnpj())) {
            throw new ValidationException("CNPJ already exists: " + request.getCnpj());
        }

        return createUserWithType(request, UserType.PJ, user -> {
            LegalEntity legalEntity = builderLegalEntity(request, user);
            user.setLegalEntity(legalEntity);
        });
    }

    public UserResponse findUserById(Long id, String authenticatedUserEmail, UserRole authenticatedUserRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado para o ID: " + id));

        if (authenticatedUserRole != UserRole.ADMIN && !user.getUserEmail().equals(authenticatedUserEmail)) {
            throw new AccessDeniedException("Usuário não autorizado para acessar este usuário");
        }

        return toUserResponse(user);    
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateRequest request, String authenticatedUserEmail, UserRole authenticatedUserRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado para atualização: " + id));

        if (authenticatedUserRole != UserRole.ADMIN && !user.getUserEmail().equals(authenticatedUserEmail)) {
            throw new AccessDeniedException("Usuário não autorizado para atualizar este usuário");
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        if (request.getUserEmail() != null && !request.getUserEmail().isBlank()) {
            validateEmailUniqueness(request.getUserEmail(), id);
            user.setUserEmail(request.getUserEmail());
        }

        if (request.getUserPassword() != null && !request.getUserPassword().isBlank()) {
            user.setUserPassword(passwordEncoder.encode(request.getUserPassword()));
        }

        if (user.getUserType() == UserType.PF) {
            updateNaturalPerson(user, request, id);     
        } else if (user.getUserType() == UserType.PJ) {
            updateLegalEntity(user, request, id);
        }


        User updatedUser = userRepository.save(user);
        return toUserResponse(updatedUser);

    }

    @Transactional
    public void deleteUser(Long id, String authenticatedUserEmail, UserRole authenticatedUserRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não econtrado"));

        if (authenticatedUserRole != UserRole.ADMIN && !user.getUserEmail().equals(authenticatedUserEmail)) {
            throw new AccessDeniedException("Você não tem permissão para deletar este usuário");
        }
        userRepository.delete(user);
    }

    private UserResponse createUserWithType(CreateUserRequest request, UserType type, Consumer<User> setChildEntity) {
        User user = builderUserFromCommonFilds(request, type);
        setChildEntity.accept(user);
        User savedUser = userRepository.save(user);
        return toUserResponse(savedUser);
    }

    private User builderUserFromCommonFilds(CreateUserRequest request, UserType type) {
        if (userRepository.existsByUserEmail(request.getUserEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + request.getUserEmail());
        }
        User user = new User();
        user.setName(request.getName());
        user.setUserEmail(request.getUserEmail());
        user.setUserPassword(passwordEncoder.encode(request.getUserPassword()));
        user.setUserStatus(UserStatus.ATIVO);
        user.setUserRole(request.getUserRole());
        user.setUserType(type);

        return user;
    }

    private NaturalPerson builderNaturalPerson(CreateNaturalPersonRequest request, User user) {
        NaturalPerson naturalPerson = new NaturalPerson();
        naturalPerson.setUser(user);
        naturalPerson.setCpf(request.getCpf());
        naturalPerson.setBirthDate(request.getBirthDate());
        
        return naturalPerson;
    }

    private LegalEntity builderLegalEntity(CreateLegalEntityRequest request, User user) {
        LegalEntity legalEntity = new LegalEntity();
        legalEntity.setUser(user);
        legalEntity.setCnpj(request.getCnpj());
        legalEntity.setCompanyName(request.getCompanyName());
        legalEntity.setStateRegistration(request.getStateRegistration());

        return legalEntity;
    }

    private void updateNaturalPerson(User user, UpdateRequest request, Long userId) {
        NaturalPerson naturalPerson = user.getNaturalPerson();

        if (naturalPerson == null) {
            throw new ValidationException("Dados de pessoa física não encontrados para o usuário com ID: " + userId);
        }

        if (request.getCpf() != null && !request.getCpf().isBlank()) {
            validateCpfUniqueness(request.getCpf(), userId);
            naturalPerson.setCpf(request.getCpf());
        }
        if (request.getBirthDate() != null) {
            naturalPerson.setBirthDate(request.getBirthDate());
        }
    }

    private void updateLegalEntity(User user, UpdateRequest request, Long userId) {
        LegalEntity legalEntity = user.getLegalEntity();

        if (legalEntity == null) {
            throw new ValidationException("Dados de pessoa jurídica não encontrados para o usuário com ID: " + userId);
        }

        if (request.getCnpj() != null && !request.getCnpj().isBlank()) {
            validateCnpjUniqueness(request.getCnpj(), userId);
            legalEntity.setCnpj(request.getCnpj());
        }
        if (request.getCompanyName() != null && !request.getCompanyName().isBlank()) {
            legalEntity.setCompanyName(request.getCompanyName());
        }
        if (request.getStateRegistration() != null && !request.getStateRegistration().isBlank()) {
            legalEntity.setStateRegistration(request.getStateRegistration());
        }
    }

    private void validateEmailUniqueness(String email, Long currentUserId) {
        userRepository.findByUserEmail(email)
                .filter(user -> !user.getUserId().equals(currentUserId))
                .ifPresent(user -> {
                    throw new EmailAlreadyExistsException("Email already exists: " + email);
                });
    }

    private void validateCpfUniqueness(String cpf, Long currentUserId) {
        naturalPersonRepository.findByCpf(cpf)
                .filter(naturalPerson -> !naturalPerson.getUser().getUserId().equals(currentUserId))
                .ifPresent(naturalPerson -> {
                    throw new ValidationException("CPF already exists: " + cpf);
                });
    }

    private void validateCnpjUniqueness(String cnpj, Long currentUserId) {
        legalEntityRepository.findByCnpj(cnpj)
                .filter(legalEntity -> !legalEntity.getUser().getUserId().equals(currentUserId))
                .ifPresent(legalEntity -> {
                    throw new ValidationException("CNPJ already exists: " + cnpj);
                });
    }

    private UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setUserName(user.getName());
        response.setUserEmail(user.getUserEmail());
        response.setUserRole(user.getUserRole());
        response.setUserStatus(user.getUserStatus());
        response.setUserType(user.getUserType());
        response.setUserCreatedAt(user.getUserCreatedAt());

        if (user.getUserType() == UserType.PF && user.getNaturalPerson() != null) {
            NaturalPerson naturalPerson = user.getNaturalPerson();
            response.setCpf(naturalPerson.getCpf());
            response.setBirthDate(naturalPerson.getBirthDate());
            
        } else if (user.getUserType() == UserType.PJ && user.getLegalEntity() != null) {
            LegalEntity legalEntity = user.getLegalEntity();
            response.setCompanyName(legalEntity.getCompanyName());
            response.setStateRegistration(legalEntity.getStateRegistration());
        }

        return response;

    }
}