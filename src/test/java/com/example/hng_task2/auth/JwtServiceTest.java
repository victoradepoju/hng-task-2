package com.example.hng_task2.auth;

import com.example.hng_task2.entity.User;
import com.example.hng_task2.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateToken() {
        String username = "testUsername";
        when(user.getUsername()).thenReturn(username);

        String token = jwtService.generateToken(user);
        assertNotNull(token);

        String usernameFromToken = jwtService.extractUsername(token);
        assertEquals(username, usernameFromToken);

        boolean isValid = jwtService.isValid(token, user);
        assertTrue(isValid);
    }

    @Test
    void testIsTokenExpired() {
        // Mock user object behavior
        String username = "testUsername";
        when(user.getUsername()).thenReturn(username);

        // Generate token
        String token = jwtService.generateToken(user);
        assertNotNull(token);

        // Extract expiration date from token
        Date expirationDate = jwtService.extractExpiration(token);
        assertNotNull(expirationDate);

        // Ensure the username is not null
        String usernameFromToken = jwtService.extractUsername(token);
        assertNotNull(usernameFromToken);

        // Calculate expected expiration time
        long expectedExpirationTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000);
        long actualExpirationTime = expirationDate.getTime();

        // Allow a buffer of a few seconds for timing discrepancies
        long buffer = 5000;

        // Check if the token expires approximately after 24 hours within the buffer range
        assertTrue(Math.abs(expectedExpirationTime - actualExpirationTime) < buffer,
                "Token should expire approximately after 24 hours");

        // Ensure the token is valid immediately after creation
        assertTrue(jwtService.isValid(token, user));

        // Fast-forward the system clock by 24 hours and ensure the token is expired
        Date currentDate = new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000));
        assertTrue(expirationDate.before(currentDate));
    }

}