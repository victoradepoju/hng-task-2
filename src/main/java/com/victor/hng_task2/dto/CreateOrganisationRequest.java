package com.victor.hng_task2.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateOrganisationRequest (
    String name,
    String description
) {}
