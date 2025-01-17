package com.victor.hng_task2.service;

import com.victor.hng_task2.dto.AppResponse;
import com.victor.hng_task2.dto.LoginRequest;
import com.victor.hng_task2.entity.User;
import com.victor.hng_task2.exception.EmailAlreadyExistsException;
import com.victor.hng_task2.jwt.JwtService;
import com.victor.hng_task2.mapper.UserMapper;
import com.victor.hng_task2.repository.UserRepository;
import com.victor.hng_task2.dto.RegisterRequest;
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
    public AppResponse register(RegisterRequest registerRequest) {
        validateEmailNotExist(registerRequest.email());

        User user = userMapper.createUserFromRegisterRequest(registerRequest);
//        userRepository.save(user);
        String result = authenticate(registerRequest.email(), registerRequest.password(), user);
        return buildAuthResponse(result, user, "register");
    }

    @Transactional
    public AppResponse login(LoginRequest loginRequest) {
        var user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = authenticate(loginRequest.email(), loginRequest.password(), user);


        return buildAuthResponse(token, user, "login");
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

    private AppResponse buildAuthResponse(String result, User user, String path) {
        return AppResponse.builder()
                .status("success")
                .message(path.equals("login") ? "Login successful"
                        : "Registration successful")
                .data(userMapper.toUserData(result, user))
                .build();
    }
}