package com.rmsvg.livestock.dto;

import jakarta.validation.constraints.NotBlank;

public record StatusUpdateRequest(@NotBlank String status) {}
