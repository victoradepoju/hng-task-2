package com.victor.hng_task2.controller;

import com.victor.hng_task2.dto.AddUserToOrganisationRequest;
import com.victor.hng_task2.dto.AppResponse;
import com.victor.hng_task2.dto.CreateOrganisationRequest;
import com.victor.hng_task2.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/organisations")
public class OrganisationController {
    private final OrganisationService organisationService;

    @GetMapping
    public ResponseEntity<AppResponse> getAllowedOrganisations(
            Authentication activeUser
    ) {
        return ResponseEntity.ok(organisationService.getAllowedOrganisation(activeUser));
    }

    @GetMapping("/{orgId}")
    public ResponseEntity<AppResponse> getAllowedOrganisationById(
            @PathVariable(name = "orgId") String orgId,
            Authentication activeUser
    ) {
        return ResponseEntity.ok(organisationService.getAllowedOrganisationById(
                orgId,
                activeUser
        ));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<AppResponse> createOrganisation(
            @RequestBody CreateOrganisationRequest orgRequest,
            Authentication activeUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                organisationService.create(orgRequest, activeUser)
        );
    }

    @PostMapping(value = "/{orgId}/users", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<AppResponse> addUserToOrganisation(
            @PathVariable(name = "orgId") String orgId,
            @RequestBody AddUserToOrganisationRequest request,
            Authentication activeUser
    ) {
        return ResponseEntity.ok(organisationService.addToOrganisation(
                orgId,
                request,
                activeUser
        ));
    }
}
