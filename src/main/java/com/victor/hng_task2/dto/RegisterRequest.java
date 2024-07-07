package com.victor.hng_task2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest (
        @NotNull
        @NotBlank(message = "Firstname is needed for registration")
        String firstName,

        @NotNull
        @NotBlank(message = "Lastname is needed for registration")
        String lastName,

        @NotNull
        @Email(message = "Email is not properly formatted")
        @NotBlank(message = "Email is needed for registration")
        String email,

        @NotNull
        @NotBlank(message = "Password is needed for registration")
        String password,

        String phone
) {
}
