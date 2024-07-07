package com.victor.hng_task2.service;

import com.victor.hng_task2.dto.AppResponse;
import com.victor.hng_task2.dto.DataResponse;
import com.victor.hng_task2.dto.serializer.ShowNullWrapper;
import com.victor.hng_task2.entity.User;
import com.victor.hng_task2.exception.NotPermittedException;
import com.victor.hng_task2.repository.OrganisationRepository;
import com.victor.hng_task2.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final OrganisationRepository organisationRepository;

    public AppResponse allowedUsers(String userId, Authentication loggedInUser) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        User activeUser = (User) loggedInUser.getPrincipal();
        User active = userRepository.findByEmail(activeUser.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        if (active.getUserId().equals(userId) ||
                active.getOrganisations().stream()
                        .anyMatch(organisation -> organisation.getUsers().contains(user)) ||
                organisationRepository.findByCreator(active).stream()
                        .anyMatch(organisation -> organisation.getUsers().contains(user))
        ) {
            return AppResponse.builder()
                    .status("success")
                    .message("User found")
                    .data(
                            DataResponse.builder()
                                    .userId(userId)
                                    .firstName(user.getFirstName())
                                    .lastName(user.getLastName())
                                    .email(user.getEmail())
                                    .phone(ShowNullWrapper.of(user.getPhone()))
                                    .build()
                    )
                    .build();
        } else {
            throw new NotPermittedException("Client error");
        }
    }
}
