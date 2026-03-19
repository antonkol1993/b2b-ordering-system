package com.yourapp.telegram.dto;

import jakarta.validation.constraints.NotBlank;

public record TelegramAuthDto(
        @NotBlank
        String initData
) {}

