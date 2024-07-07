package com.victor.hng_task2.dto;

import lombok.Builder;

@Builder
public record OrganisationListResponse(
        String orgId,
        String name,
        String description
) {
}
