package com.victor.hng_task2.auth;

import com.victor.hng_task2.dto.AppResponse;
import com.victor.hng_task2.dto.DataResponse;
import com.victor.hng_task2.dto.OrganisationListResponse;
import com.victor.hng_task2.entity.Organisation;
import com.victor.hng_task2.entity.User;
import com.victor.hng_task2.mapper.OrganisationMapper;
import com.victor.hng_task2.repository.OrganisationRepository;
import com.victor.hng_task2.repository.UserRepository;
import com.victor.hng_task2.service.OrganisationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrganisationServiceTest {

    @InjectMocks
    private OrganisationService organisationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrganisationRepository organisationRepository;

    @Mock
    private OrganisationMapper organisationMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllowedOrganisation() {
        // Mock authenticated user
        User mockUser = new User();
        mockUser.setUserId("testUserId");
        mockUser.setOrganisations(new ArrayList<>()); // Ensure no organisations initially

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mock repository responses
        Organisation org1 = new Organisation("org1", "Description 1");
        org1.setOrgId("orgId1");
        org1.setCreator(mockUser); // Set the creator explicitly

        Organisation org2 = new Organisation("org2", "Description 2");
        org2.setOrgId("orgId2");
        org2.setCreator(mockUser); // Set the creator explicitly

        List<Organisation> userOrgs = List.of(org1);
        List<Organisation> createdOrgs = List.of(org1, org2);

        when(userRepository.findById("testUserId")).thenReturn(Optional.of(mockUser));
        when(organisationRepository.findByCreator(mockUser)).thenReturn(createdOrgs);

        // Mock mapper response
        OrganisationListResponse org1Response = new OrganisationListResponse("orgId1", "org1", "Description 1");
        OrganisationListResponse org2Response = new OrganisationListResponse("orgId2", "org2", "Description 2");

        when(organisationMapper.mapToOrganisationListResponse(org1)).thenReturn(org1Response);
        when(organisationMapper.mapToOrganisationListResponse(org2)).thenReturn(org2Response);

        // Invoke service method
        AppResponse appResponse = organisationService.getAllowedOrganisation(authentication);

        // Assertions
        assertNotNull(appResponse);
        assertEquals("success", appResponse.getStatus());
        assertEquals("Organisations found", appResponse.getMessage());

        DataResponse dataResponse = appResponse.getData();
        assertNotNull(dataResponse);
        List<OrganisationListResponse> organisationListResponses = dataResponse.organisations();
        assertNotNull(organisationListResponses);
        assertEquals(2, organisationListResponses.size()); // Ensure both org1 and org2 are returned

        // Verify interactions
        verify(userRepository, times(1)).findById("testUserId");
        verify(organisationRepository, times(1)).findByCreator(mockUser);
        verify(organisationMapper, times(1)).mapToOrganisationListResponse(org1);
        verify(organisationMapper, times(1)).mapToOrganisationListResponse(org2);
    }

    @Test
    void testGetAllowedOrganisationWithUnknownOrganisation() {
        // Mock authenticated user
        User mockUser = User.builder()
                .userId("testUserId")
                .build();

        // Mock user's organisations
        Organisation org1 = Organisation.builder()
                .orgId("orgId1")
                .name("org1")
                .description("Description 1")
                .creator(mockUser) // Set creator
                .build();

        Organisation org2 = Organisation.builder()
                .orgId("orgId2")
                .name("org2")
                .description("Description 2")
                .creator(mockUser) // Set creator
                .build();

        // Organisation not associated with the user
        Organisation org3 = Organisation.builder()
                .orgId("orgId3")
                .name("org3")
                .description("Description 3")
                .creator(new User()) // Set different creator
                .build();

        List<Organisation> userOrgs = new ArrayList<>(List.of(org1));
        List<Organisation> createdOrgs = new ArrayList<>(List.of(org2));

        mockUser.setOrganisations(userOrgs);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mock repository responses
        when(userRepository.findById("testUserId")).thenReturn(Optional.of(mockUser));
        when(organisationRepository.findByCreator(mockUser)).thenReturn(createdOrgs);

        // Mock mapper response
        OrganisationListResponse org1Response = new OrganisationListResponse("orgId1", "org1", "Description 1");
        OrganisationListResponse org2Response = new OrganisationListResponse("orgId2", "org2", "Description 2");

        when(organisationMapper.mapToOrganisationListResponse(org1)).thenReturn(org1Response);
        when(organisationMapper.mapToOrganisationListResponse(org2)).thenReturn(org2Response);

        // Invoke service method
        AppResponse appResponse = organisationService.getAllowedOrganisation(authentication);

        // Assertions
        assertNotNull(appResponse);
        assertEquals("success", appResponse.getStatus());
        assertEquals("Organisations found", appResponse.getMessage());

        DataResponse dataResponse = appResponse.getData();
        assertNotNull(dataResponse);
        List<OrganisationListResponse> organisationListResponses = dataResponse.organisations();
        assertNotNull(organisationListResponses);
        assertEquals(2, organisationListResponses.size()); // Ensure org1 and org2 are returned
        assertEquals("orgId1", organisationListResponses.get(0).orgId());
        assertEquals("org1", organisationListResponses.get(0).name());
        assertEquals("orgId2", organisationListResponses.get(1).orgId());
        assertEquals("org2", organisationListResponses.get(1).name());

        // Verify interactions
        verify(userRepository, times(1)).findById("testUserId");
        verify(organisationRepository, times(1)).findByCreator(mockUser);
        verify(organisationMapper, times(1)).mapToOrganisationListResponse(org1);
        verify(organisationMapper, times(1)).mapToOrganisationListResponse(org2);
        verify(organisationMapper, never()).mapToOrganisationListResponse(org3); // Ensure org3 is not mapped
    }
}