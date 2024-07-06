package com.example.hng_task2.controller;

import com.example.hng_task2.dto.AuthResponse;
import com.example.hng_task2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<AuthResponse> getAllowedUser(
            @PathVariable(name = "id") String id,
            Authentication loggedInUser
    ) {
        return ResponseEntity.ok(userService.allowedUsers(id, loggedInUser));
    }
}
