package com.ecommerce.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long userId;
    private String userName;
    private String userEmail;
    private String userStatus;
    private LocalDateTime userCreatedAt;

    public Long getUserId() {
        return userId;
    }
    public String getUserName() {
        return userName;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public String getUserStatus() {
        return userStatus;
    }
    public LocalDateTime getUserCreatedAt() {
        return userCreatedAt;
    }
    

}
