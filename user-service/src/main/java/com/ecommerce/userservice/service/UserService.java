package com.ecommerce.userservice.service;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.userservice.dto.CreateAdminRequest;
import com.ecommerce.userservice.dto.CreateLegalEntityRequest;
import com.ecommerce.userservice.dto.CreateNaturalPersonRequest;
import com.ecommerce.userservice.dto.CreateUserRequest;
import com.ecommerce.userservice.dto.LegalEntityResponse;
import com.ecommerce.userservice.dto.NaturalPersonResponse;
import com.ecommerce.userservice.dto.UpdateRequest;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.enums.UserRole;
import com.ecommerce.userservice.enums.UserStatus;
import com.ecommerce.userservice.enums.UserType;
import com.ecommerce.userservice.exception.EmailAlreadyExistsException;
import com.ecommerce.userservice.exception.UserNotFoundException;
import com.ecommerce.userservice.exception.ValidationException;
import com.ecommerce.userservice.model.LegalEntity;
import com.ecommerce.userservice.model.NaturalPerson;
import com.ecommerce.userservice.model.User;
import com.ecommerce.userservice.repository.LegalEntityRepository;
import com.ecommerce.userservice.repository.NaturalPersonRepository;
import com.ecommerce.userservice.repository.UserRepository;

import jakarta.transaction.Transactional;

/**
 * Coordena as operações de domínio relacionadas a usuários.
 *
 * <p>
 * Centraliza a criação, consulta, atualização e exclusão de usuários, além da
 * montagem da resposta adequada para cada tipo de cadastro. A classe também
 * aplica as regras de unicidade e autorização que não cabem na camada de
 * persistência.
 *
 * @since 1.0
 */
@Service
public class UserService {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private NaturalPersonRepository naturalPersonRepository;
    private LegalEntityRepository legalEntityRepository;

    public UserService() {

    }

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            NaturalPersonRepository naturalPersonRepository, LegalEntityRepository legalEntityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.naturalPersonRepository = naturalPersonRepository;
        this.legalEntityRepository = legalEntityRepository;
    }

    /**
     * Cria um usuário administrador com status ativo e tipo técnico.
     *
     * <p>
     * A operação falha quando o e-mail já está em uso.
     *
     * @param request dados necessários para cadastrar o administrador.
     * @return a representação do usuário criado.
     * @throws EmailAlreadyExistsException quando o e-mail informado já existe.
     */
    public UserResponse createAdmin(CreateAdminRequest request) {
        if (userRepository.existsByUserEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        User user = new User();
        user.setName(request.getName());
        user.setUserEmail(request.getEmail());
        user.setUserPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserRole(UserRole.ADMIN);
        user.setUserStatus(UserStatus.ATIVO);
        user.setUserType(UserType.SYSTEM);

        User savedUser = userRepository.save(user);
        return toUserResponse(savedUser);
    }

    /**
     * Cria um usuário do tipo pessoa física com seus dados complementares.
     *
     * <p>
     * A operação valida a unicidade do CPF antes de persistir o cadastro
     * completo.
     *
     * @param request dados de cadastro do usuário e da pessoa física.
     * @return a representação do usuário criado.
     * @throws ValidationException         quando o CPF já estiver cadastrado.
     * @throws EmailAlreadyExistsException quando o e-mail informado já existir.
     */
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

    /**
     * Cria um usuário do tipo pessoa jurídica com seus dados complementares.
     *
     * <p>
     * A operação valida a unicidade do CNPJ antes de persistir o cadastro
     * completo.
     *
     * @param request dados de cadastro do usuário e da pessoa jurídica.
     * @return a representação do usuário criado.
     * @throws ValidationException         quando o CNPJ já estiver cadastrado.
     * @throws EmailAlreadyExistsException quando o e-mail informado já existir.
     */
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

    /**
     * Lista usuários paginados convertendo cada registro para sua visão pública.
     *
     * @param pageable definição de paginação e ordenação.
     * @return uma página com os usuários formatados para resposta.
     */
    public Page<UserResponse> listAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::toUserResponse);
    }

    /**
     * Localiza um usuário por identificador com controle de acesso.
     *
     * <p>
     * Administradores podem acessar qualquer usuário; demais perfis só acessam o
     * próprio registro.
     *
     * @param id                     identificador do usuário.
     * @param authenticatedUserEmail e-mail do usuário autenticado.
     * @param authenticatedUserRole  perfil do usuário autenticado.
     * @return a visão pública do usuário localizado.
     * @throws UserNotFoundException quando o identificador não existir.
     * @throws AccessDeniedException quando o acesso não for permitido.
     */
    public UserResponse findUserById(Long id, String authenticatedUserEmail, UserRole authenticatedUserRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado para o ID: " + id));

        if (authenticatedUserRole != UserRole.ADMIN && !user.getUserEmail().equals(authenticatedUserEmail)) {
            throw new AccessDeniedException("Usuário não autorizado para acessar este usuário");
        }

        return toUserResponse(user);
    }

    /**
     * Localiza um usuário pelo e-mail informado.
     *
     * @param email e-mail usado na busca.
     * @return a visão pública do usuário localizado.
     * @throws UserNotFoundException quando não houver usuário com o e-mail
     *                               informado.
     */
    public UserResponse findUserByEmail(String email) {
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado para o email: " + email));

        return toUserResponse(user);
    }

    /**
     * Atualiza os dados permitidos de um usuário já cadastrado.
     *
     * <p>
     * A operação respeita o perfil autenticado, valida unicidade de e-mail, CPF
     * e CNPJ quando aplicável e sincroniza os dados específicos do tipo de
     * usuário.
     *
     * @param id                     identificador do usuário.
     * @param request                dados parciais para atualização.
     * @param authenticatedUserEmail e-mail do usuário autenticado.
     * @param authenticatedUserRole  perfil do usuário autenticado.
     * @return a visão pública do usuário atualizado.
     * @throws UserNotFoundException       quando o usuário não for encontrado.
     * @throws AccessDeniedException       quando o acesso não for permitido.
     * @throws ValidationException         quando dados específicos do tipo
     *                                     estiverem ausentes ou em conflito.
     * @throws EmailAlreadyExistsException quando o novo e-mail já estiver em uso
     *                                     por outro usuário.
     */
    @Transactional
    public UserResponse updateUser(Long id, UpdateRequest request, String authenticatedUserEmail,
            UserRole authenticatedUserRole) {
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

    /**
     * Remove um usuário do cadastro.
     *
     * <p>
     * Administradores podem remover qualquer usuário; demais perfis só podem
     * remover a própria conta.
     *
     * @param id                     identificador do usuário.
     * @param authenticatedUserEmail e-mail do usuário autenticado.
     * @param authenticatedUserRole  perfil do usuário autenticado.
     * @throws UserNotFoundException quando o usuário não for encontrado.
     * @throws AccessDeniedException quando o acesso não for permitido.
     */
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

    private void fillCommonFields(UserResponse response, User user) {
        response.setUserId(user.getUserId());
        response.setUserName(user.getName());
        response.setUserEmail(user.getUserEmail());
        response.setUserStatus(user.getUserStatus());
        response.setUserRole(user.getUserRole());
        response.setUserType(user.getUserType());
        response.setUserCreatedAt(user.getUserCreatedAt());
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
        if (user.getUserType() == UserType.PF) {
            NaturalPersonResponse npResponse = new NaturalPersonResponse();
            fillCommonFields(npResponse, user);
            if (user.getNaturalPerson() != null) {
                npResponse.setCpf(user.getNaturalPerson().getCpf());
                npResponse.setBirthDate(user.getNaturalPerson().getBirthDate());
            }
            return npResponse;
        } else if (user.getUserType() == UserType.PJ) {
            LegalEntityResponse leResponse = new LegalEntityResponse();
            fillCommonFields(leResponse, user);
            if (user.getLegalEntity() != null) {
                leResponse.setCnpj(user.getLegalEntity().getCnpj());
                leResponse.setCompanyName(user.getLegalEntity().getCompanyName());
                leResponse.setStateRegistration(user.getLegalEntity().getStateRegistration());
            }
            return leResponse;
        } else if (user.getUserType() == UserType.SYSTEM) {
            UserResponse response = new UserResponse();
            fillCommonFields(response, user);
            return response;
        } else {
            throw new IllegalArgumentException("Tipo de usuário desconhecido: " + user.getUserType());
        }
    }
}