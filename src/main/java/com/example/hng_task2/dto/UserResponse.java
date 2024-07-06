package com.example.hng_task2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record UserResponse (
        String userId,
        String firstName,
        String lastName,
        String email,
        String phone,

        String orgId,
        String name
){
}
