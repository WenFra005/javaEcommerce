package com.ecommerce.userservice.dto;

public class AuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String userEmail;
    
    public AuthResponse(String token, String tokenType, Long userId, String userEmail) {
        this.token = token;
        this.tokenType = tokenType;
        this.userId = userId;
        this.userEmail = userEmail;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

}
