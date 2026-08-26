package com.rmsvg.livestock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderCreateRequest(
        @NotBlank String customerName,
        @NotBlank String phone,
        String email,
        String deliveryAddress,
        @NotNull Long livestockId,
        @NotNull BigDecimal quantity
) {}
