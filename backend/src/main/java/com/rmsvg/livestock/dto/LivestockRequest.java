package com.rmsvg.livestock.dto;

import com.rmsvg.livestock.domain.Enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record LivestockRequest(
        @NotBlank String animalCode,
        @NotNull Category category,
        @NotBlank String breed,
        Gender gender,
        String ageLabel,
        Integer ageMonths,
        BigDecimal weightKg,
        @NotNull PricingType pricingType,
        @NotNull BigDecimal price,
        BigDecimal minOrderQty,
        BigDecimal availableQty,
        String location,
        String description,
        String whyChoose,
        LivestockStatus status,
        Boolean featured
) {}
