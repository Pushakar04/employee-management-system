package com.ems.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private UserDetails testUser;

    @BeforeEach
    void setUp() {
    	
        jwtUtil = new JwtUtil();

        ReflectionTestUtils.setField(
        		jwtUtil, 
        		"secret",
                "9XTvLhZyGUa3ohnNuPPxL22/7hS2BPfaBXJKBMmgyKM=");
        
        ReflectionTestUtils.setField(
        		jwtUtil, 
        		"expirationMs", 
        		86400000L); 

        testUser = User.builder()
                .username("admin1")
                .password("admin@123")
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .build();
    }

    @Test
    void generateToken_shouldReturnNonNullNonEmptyToken() {
        String token = jwtUtil.generateToken(testUser);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void generateToken_shouldProduceThreeDotSeparatedSegments() {

    	String token = jwtUtil.generateToken(testUser);
        String[] parts = token.split("\\.");

        assertEquals(3, parts.length);
    }

    @Test
    void extractUsername_shouldReturnUsernameEmbeddedAtGeneration() {
        String token = jwtUtil.generateToken(testUser);

        String extractedUsername = jwtUtil.extractUsername(token);

        assertEquals("admin1", extractedUsername);
    }

    @Test
    void isTokenValid_shouldReturnTrueForMatchingUserAndUnexpiredToken() {
        String token = jwtUtil.generateToken(testUser);

        boolean result = jwtUtil.isTokenValid(token, testUser);

        assertTrue(result);
    }

    @Test
    void isTokenValid_shouldReturnFalseWhenUsernameDoesNotMatch() {
        String token = jwtUtil.generateToken(testUser);

        UserDetails differentUser =User
        		.builder()
                .username("Admin2")
                .password("adiuy123")
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .build();

        boolean result = jwtUtil.isTokenValid(token, differentUser);

        assertFalse(result);
    }

    @Test
    void isTokenValid_shouldReturnFalseForExpiredToken() {

    	ReflectionTestUtils.setField(jwtUtil, "expirationMs", -1000L);
        String expiredToken = jwtUtil.generateToken(testUser);

        boolean result = jwtUtil.isTokenValid(expiredToken, testUser);

        assertFalse(result);
    }
}