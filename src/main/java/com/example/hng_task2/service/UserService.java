package com.example.hng_task2.service;

import com.example.hng_task2.dto.AuthResponse;
import com.example.hng_task2.dto.DataResponse;
import com.example.hng_task2.entity.User;
import com.example.hng_task2.exception.NotPermittedException;
import com.example.hng_task2.mapper.UserMapper;
import com.example.hng_task2.repository.OrganisationRepository;
import com.example.hng_task2.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final OrganisationRepository organisationRepository;
    private final UserMapper userMapper;

    public AuthResponse allowedUsers(String userId, Authentication loggedInUser) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        User activeUser = (User) loggedInUser.getPrincipal();

        if (activeUser.getUserId().equals(userId) ||
                activeUser.getOrganisations().stream()
                        .anyMatch(organisation -> organisation.getUsers().contains(user)) ||
                organisationRepository.findByCreator(activeUser).stream()
                        .anyMatch(organisation -> organisation.getUsers().contains(user))
        ) {
            return AuthResponse.builder()
                    .status("success")
                    .message("User found")
                    .data(
                            DataResponse.builder()
                                    .userId(userId)
                                    .firstName(user.getFirstName())
                                    .lastName(user.getLastName())
                                    .email(user.getEmail())
                                    .phone(user.getPhone())
                                    .build()
                    )
                    .build();
        } else {
            throw new NotPermittedException("Client error");
        }
    }
}
