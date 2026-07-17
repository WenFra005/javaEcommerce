package com.ecommerce.userservice.Security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void testGenerateAndValidateToken() {
        String email = "test@email.com";
        Long userId = 1L;

        String token = jwtUtil.generateToken(email, userId);

        assertNotNull(token);
        assertEquals(email, jwtUtil.extractEmail(token));
        assertEquals(userId, jwtUtil.extractUserId(token));
        assertFalse(jwtUtil.isTokenExpired(token));

    }

    @Test
    void testValidateTokenWithCorrectUser() {
        String email = "test@email.com";
        String token = jwtUtil.generateToken(email, 1L);

        assertTrue(jwtUtil.validateToken(token, email));
    }

    @Test
    void testValidateTokenWithWrongUser() {
        String email = "test@email.com";
        String token = jwtUtil.generateToken(email, 1L);

        assertFalse(jwtUtil.validateToken(token, "wrong@email.com"));
    }
}
