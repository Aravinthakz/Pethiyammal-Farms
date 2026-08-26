package com.rmsvg.livestock.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PriceUpdateRequest(@NotNull BigDecimal price) {}
