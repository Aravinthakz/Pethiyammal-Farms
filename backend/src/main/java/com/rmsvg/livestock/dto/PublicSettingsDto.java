package com.rmsvg.livestock.dto;

public record PublicSettingsDto(
        String whatsappNumber,
        String phone,
        String address,
        String hours,
        String mapEmbedUrl
) {}
