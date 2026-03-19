package com.yourapp.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderItemDto(
        @NotNull
        @Positive
        Long productId,

        @NotNull
        @Positive
        Integer quantity
) {}

