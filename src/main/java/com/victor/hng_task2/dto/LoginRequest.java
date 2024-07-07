package com.victor.hng_task2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email(message = "Email is not properly formatted")
        @NotBlank(message = "Email is needed for login")
        String email,

        @NotBlank(message = "Password is needed for login")
        String password
) {
}
