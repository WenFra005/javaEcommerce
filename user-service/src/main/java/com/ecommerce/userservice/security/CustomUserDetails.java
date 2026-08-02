package com.ecommerce.userservice.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecommerce.userservice.enums.UserRole;
import com.ecommerce.userservice.model.User;

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

    public User getUser() {
        User user = new User();
        user.setUserId(userId);
        user.setUserEmail(email);
        user.setUserPassword(password);
        user.setUserRole(role);
        return user;
    }

}
