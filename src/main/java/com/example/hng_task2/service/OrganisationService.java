package com.example.hng_task2.service;

import com.example.hng_task2.dto.*;
import com.example.hng_task2.dto.serializer.ShowNullWrapper;
import com.example.hng_task2.entity.Organisation;
import com.example.hng_task2.entity.User;
import com.example.hng_task2.exception.NotPermittedException;
import com.example.hng_task2.exception.OrganisationNameEmptyException;
import com.example.hng_task2.mapper.OrganisationMapper;
import com.example.hng_task2.repository.OrganisationRepository;
import com.example.hng_task2.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganisationService {
    private final OrganisationRepository organisationRepository;
    private final OrganisationMapper organisationMapper;
    private final UserRepository userRepository;

    @Transactional
    public AppResponse getAllowedOrganisation(Authentication activeUser) {
        User user = (User) activeUser.getPrincipal();

        // fixed lazy initialization error...
        user = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Organisation> organisations = new ArrayList<>(user.getOrganisations());

        // the organisation created by the user
        organisations.addAll(organisationRepository.findByCreator(user));

        // Check if user is authorized to access each organisation
        for (Organisation org : organisations) {
            if (!isActiveUserAllowed(user, org)) {
                throw new NotPermittedException("User not allowed to access organisation: " + org.getName());
            }
        }

        List<Organisation> allowedOrganisations = organisations.stream()
                .distinct()
                .toList();

        List<OrganisationListResponse> organisationListResponses = allowedOrganisations
                .stream()
                .map(organisationMapper::mapToOrganisationListResponse)
                .toList();

        return AppResponse.builder()
                .status("success")
                .message("Organisations found")
                .data(DataResponse.builder()
                        .organisations(organisationListResponses)
                        .build())
                .build();
    }

    @Transactional
    public AppResponse getAllowedOrganisationById(
            String orgId,
            Authentication activeUser
    ) {
        User user = (User) activeUser.getPrincipal();

        userRepository.findById(user.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

//        List<Organisation> organisations = new ArrayList<>(user.getOrganisations());
//        organisations.addAll(organisationRepository.findByCreator(user));

        Organisation organisation = organisationRepository.findById(orgId)
                .orElseThrow(() -> new EntityNotFoundException("Organisation not found"));

        // Find the organisation by orgId
//        Organisation organisation = organisations.stream()
//                .filter(org -> org.getOrgId().equals(orgId))
//                .findFirst()
//                .orElseThrow(() -> new NotPermittedException("Organisation not found or user not allowed to access it"));

        return AppResponse.builder()
                .status("success")
                .message("Organisation found")
                .data(DataResponse.builder()
                        .orgId(organisation.getOrgId())
                        .name(organisation.getName())
                        .description(ShowNullWrapper.of(organisation.getDescription()))
                        .build())
                .build();
    }

    private boolean isActiveUserAllowed(User user, Organisation organisation) {
        return user.getOrganisations().contains(organisation) ||
                organisation.getCreator().equals(user);
    }

    @Transactional
    public AppResponse create(
            CreateOrganisationRequest orgRequest,
            Authentication activeUser
    ) {
        if (orgRequest.name() == null) {
            throw new OrganisationNameEmptyException(
                    "An Organisation must have a name"
            );
        }
        User user = (User) activeUser.getPrincipal();
        user = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Organisation organisation = Organisation.builder()
                .name(orgRequest.name())
                .description(orgRequest.description())
                .users(List.of(user))
                .creator(user)
                .build();
        organisationRepository.save(organisation);
        return AppResponse.builder()
                .status("success")
                .message("Organisation created successfully")
                .data(
                        DataResponse.builder()
                                .orgId(organisation.getOrgId())
                                .name(organisation.getName())
                                .description(ShowNullWrapper.of(organisation.getDescription()))
                                .build()
                )
                .build();
    }

    @Transactional
    public AppResponse addToOrganisation(
            String orgId,
            AddUserToOrganisationRequest request,
            Authentication activeUser
    ) {
        User user = (User) activeUser.getPrincipal();
        userRepository.findById(user.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Find the organisation by orgId
        Organisation organisation = organisationRepository.findById(orgId)
                .orElseThrow(() -> new EntityNotFoundException("Organisation not found"));

        // todo: maybe add a check to make sure orgId is not empty??

//         Check if the active user is allowed to add users to the organisation
//        if (!organisation.getCreator().getUserId().equals(user.getUserId())) {
//            throw new NotPermittedException("User not allowed to add users to this organisation");
//        }

        // Find the user to be added by userId
        User userToAdd = userRepository.findById(request.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (organisation.getUsers().contains(userToAdd)) {
            return AppResponse.builder()
                    .status("success")
                    .message("User added to organisation successfully")
                    .build();
        }

        organisation.getUsers().add(userToAdd);
        userToAdd.getOrganisations().add(organisation);

        organisationRepository.save(organisation);
        userRepository.save(userToAdd);

        return AppResponse.builder()
                .status("success")
                .message("User added to organisation successfully")
                .build();
    }
}
