package com.ecommerce.userservice.Security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "testSecretKey12345678901234567890");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", 900000);
    }

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
