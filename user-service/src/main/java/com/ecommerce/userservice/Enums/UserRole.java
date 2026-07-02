package com.ecommerce.userservice.Enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserRole {
    ADMIN, CLIENTE, VENDEDOR, FORNECEDOR;

    @JsonCreator
    public static UserRole fromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("UserRole cannot be null or empty");
        }

        try {
            return UserRole.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                String.format("Invalid value for 'userRole': '%s'. Allowed values: %s", value, Arrays.toString(UserRole.values()))
            );
        }
    }
    
}
