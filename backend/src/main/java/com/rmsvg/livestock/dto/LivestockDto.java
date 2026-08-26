package com.rmsvg.livestock.dto;

import com.rmsvg.livestock.domain.Enums.*;
import com.rmsvg.livestock.entity.Livestock;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record LivestockDto(
        Long id,
        String animalCode,
        Category category,
        String breed,
        String title,
        Gender gender,
        String ageLabel,
        Integer ageMonths,
        BigDecimal weightKg,
        PricingType pricingType,
        BigDecimal price,
        BigDecimal minOrderQty,
        BigDecimal availableQty,
        String location,
        String description,
        String whyChoose,
        LivestockStatus status,
        boolean featured,
        Instant createdAt,
        List<ImageDto> images
) {
    public static LivestockDto from(Livestock l) {
        String title = l.getBreed();
        if (l.getGender() != null && l.getPricingType() == PricingType.FIXED) {
            title = l.getBreed() + " - " + (l.getGender() == Gender.MALE ? "Male" : "Female");
        }
        List<ImageDto> images = l.getImages().stream()
                .map(img -> new ImageDto(img.getId(), img.getImageUrl(), img.isPrimaryImage()))
                .toList();
        return new LivestockDto(
                l.getId(), l.getAnimalCode(), l.getCategory(), l.getBreed(), title, l.getGender(),
                l.getAgeLabel(), l.getAgeMonths(), l.getWeightKg(), l.getPricingType(), l.getPrice(),
                l.getMinOrderQty(), l.getAvailableQty(), l.getLocation(), l.getDescription(),
                l.getWhyChoose(), l.getStatus(), l.isFeatured(), l.getCreatedAt(), images
        );
    }
}
