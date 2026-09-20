package com.ecommerce.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.userservice.model.User;
import com.ecommerce.userservice.repository.UserRepository;
import com.ecommerce.userservice.security.CustomUserDetails;

/**
 * Adapta o usuário persistido para o contrato do Spring Security.
 *
 * <p>
 * A implementação resolve o usuário pelo e-mail porque esse é o identificador
 * usado no fluxo de autenticação do serviço.
 *
 * @since 0.1.0
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService() {

    }

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carrega os detalhes de autenticação a partir do e-mail informado.
     *
     * @param email e-mail usado como nome de usuário no processo de autenticação.
     * @return a instância de {@link CustomUserDetails} associada ao usuário.
     * @throws UsernameNotFoundException quando o e-mail não estiver cadastrado.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));

        return new CustomUserDetails(user);
    }

}
