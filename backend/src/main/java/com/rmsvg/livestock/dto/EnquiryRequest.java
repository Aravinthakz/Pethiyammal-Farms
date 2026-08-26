package com.rmsvg.livestock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EnquiryRequest(
        @NotBlank String fullName,
        @NotBlank String phone,
        Long livestockId,
        String productSelection,
        @NotNull BigDecimal quantity,
        LocalDate preferredDate,
        String deliveryLocation,
        String message
) {}
