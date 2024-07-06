package com.example.hng_task2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record DataResponse(
        // for authentication
        String accessToken,
        UserResponse user,

        // api/users/:id
        String userId,
        String firstName,
        String lastName,
        String email,
        String phone,

        // "organisations": [{}]
        List<OrganisationListResponse> organisations,

        // organisation by id
        String orgId,
        String name,
        String description
) {

}
