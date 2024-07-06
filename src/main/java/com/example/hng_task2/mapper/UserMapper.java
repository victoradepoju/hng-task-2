package com.example.hng_task2.mapper;

import com.example.hng_task2.dto.DataResponse;
import com.example.hng_task2.dto.RegisterRequest;
import com.example.hng_task2.dto.UserResponse;
import com.example.hng_task2.entity.Organisation;
import com.example.hng_task2.entity.User;
import com.example.hng_task2.repository.OrganisationRepository;
import com.example.hng_task2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OrganisationRepository orgaisationRepository;

    public User createUserFromRegisterRequest(RegisterRequest registerRequest) {
        User user = User.builder()
                .firstName(registerRequest.firstName())
                .lastName(registerRequest.lastName())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .phone(registerRequest.phone())
                .build();

        user = userRepository.save(user);

        Organisation organisation = new Organisation();
        organisation.setName(registerRequest.firstName() + "'s Organisation");
        // use a mutable list...
        organisation.setUsers(new ArrayList<>(List.of(user)));
        organisation = orgaisationRepository.save(organisation);

        // using a mutable list again
        user.setOrganisations(new ArrayList<>(List.of(organisation)));
        user = userRepository.save(user);
        return user;
    }

    public DataResponse toUserData(String result, User user) {
        return DataResponse.builder()
                .accessToken(result)
                .user(
                        UserResponse.builder()
                                .userId((user.getUserId()).toString())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .email(user.getEmail())
                                .phone(user.getPhone())
                                .build()
                )
                .build();
    }
}
