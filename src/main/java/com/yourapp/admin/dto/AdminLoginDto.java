package com.yourapp.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AdminLoginDto(
        @Email
        @NotBlank
        String email,

        @NotBlank
        String password
) {}

