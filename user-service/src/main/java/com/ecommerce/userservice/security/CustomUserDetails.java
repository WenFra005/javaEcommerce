package com.ecommerce.userservice.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecommerce.userservice.enums.UserRole;
import com.ecommerce.userservice.model.User;

/**
 * Adapta {@link User} para o contrato exigido pelo Spring Security.
 *
 * <p>
 * A implementação encapsula o estado mínimo necessário para autenticação e
 * autorização sem expor o modelo de domínio diretamente à infraestrutura de
 * segurança.
 *
 * @since 0.1.0
 */
public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final String email;
    private final String password;
    private final UserRole role;

    public CustomUserDetails(User user) {
        this.userId = user.getUserId();
        this.email = user.getUserEmail();
        this.password = user.getUserPassword();
        this.role = user.getUserRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;

    }

    /**
     * Reconstrói uma instância enxuta de {@link User} com os dados necessários
     * para consumo pela camada de autorização.
     *
     * @return usuário com os campos mínimos carregados.
     */
    public User getUser() {
        User user = new User();
        user.setUserId(userId);
        user.setUserEmail(email);
        user.setUserPassword(password);
        user.setUserRole(role);
        return user;
    }

}
