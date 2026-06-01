package com.ecommerce.demo.Model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long userId;
    protected String name;

    @Email
    protected String userEmail;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    protected String userPassword;

    @Enumerated(EnumType.STRING)
    protected UserStatus userStatus;

    @CreationTimestamp
    protected LocalDateTime userCreatedAt;

    public User() {
    }

    public User(Long userId, String name, String userEmail, String userPassword, UserStatus userStatus, LocalDateTime userCreatedAt) {
        this.userId = userId;
        this.name = name;
        this.userEmail = userEmail  ;
        this.userPassword = userPassword;
        this.userStatus = userStatus;
        this.userCreatedAt = userCreatedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public LocalDateTime getUserCreatedAt() {
        return userCreatedAt;
    }

    public void setUserCreatedAt(LocalDateTime userCreatedAt) {
        this.userCreatedAt = userCreatedAt;
    }

}
