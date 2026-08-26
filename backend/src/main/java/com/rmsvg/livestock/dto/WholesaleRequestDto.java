package com.rmsvg.livestock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record WholesaleRequestDto(
        @NotBlank String businessName,
        @NotBlank String contactName,
        @NotBlank String phone,
        @NotBlank String product,
        @NotNull BigDecimal quantity,
        BigDecimal budget,
        String location,
        LocalDate requiredDate,
        String message
) {}
