package com.example.hng_task2.mapper;

import com.example.hng_task2.dto.OrganisationListResponse;
import com.example.hng_task2.entity.Organisation;
import org.springframework.stereotype.Service;

@Service
public class OrganisationMapper {
    public OrganisationListResponse mapToOrganisationListResponse(
            Organisation organisation
    ) {
        return OrganisationListResponse.builder()
                .orgId(organisation.getOrgId())
                .name(organisation.getName())
                .description(organisation.getDescription())
                .build();
    }
}
