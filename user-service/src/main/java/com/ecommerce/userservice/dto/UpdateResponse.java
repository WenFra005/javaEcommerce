package com.ecommerce.userservice.dto;

public class UpdateResponse {

    private UserResponse userResponse;
    private String token;
    
    public UpdateResponse(UserResponse userResponse, String token) {
        this.userResponse = userResponse;
        this.token = token;
    }

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public String getToken() {
        return token;
    }

    

}
