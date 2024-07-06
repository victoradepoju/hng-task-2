package com.example.hng_task2.service;

import com.example.hng_task2.dto.*;
import com.example.hng_task2.entity.User;
import com.example.hng_task2.exception.EmailAlreadyExistsException;
import com.example.hng_task2.jwt.JwtService;
import com.example.hng_task2.mapper.UserMapper;
import com.example.hng_task2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        validateEmailNotExist(registerRequest.email());

        User user = userMapper.createUserFromRegisterRequest(registerRequest);
//        userRepository.save(user);
        String result = authenticate(registerRequest.email(), registerRequest.password(), user);
        return buildAuthResponse(result, user);
    }

    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        var user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = authenticate(loginRequest.email(), loginRequest.password(), user);


        return buildAuthResponse(token, user);
    }

    private void validateEmailNotExist(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
    }

    private String authenticate(String email, String password, User user) {
        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(email, password);

        try {
            authenticationManager.authenticate(authenticationRequest);

            return jwtService.generateToken(user);
        } catch (AuthenticationException e) {
            // would probably never give an exception, but...
            throw new BadCredentialsException(""); // todo: something may be wrong here
        }
    }

    private AuthResponse buildAuthResponse(String result, User user) {
        return AuthResponse.builder()
                .status("success")
                .message("Registration successful")
                .data(userMapper.toUserData(result, user))
                .build();
    }
}