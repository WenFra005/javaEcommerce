package com.ecommerce.userservice.Model;


import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue
    @Column(name="id",nullable = false, unique = true)
    private Long id;

    @Column(name="token",nullable = false, unique = true)
    private String token;

    @Column(name="expiry_date",nullable = false)
    private Instant expiryDate;

    @Column(name="revoked",nullable = false)
    private boolean revoked;

    @OneToOne(fetch = FetchType.LAZY)
    @Column(name="user_id",nullable = false)
    private User user;

    public RefreshToken() {
    }

    public RefreshToken(Long id, String token, Instant expiryDate, boolean revoked, User user) {
        this.id = id;
        this.token = token;
        this.expiryDate = expiryDate;
        this.revoked = revoked;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
