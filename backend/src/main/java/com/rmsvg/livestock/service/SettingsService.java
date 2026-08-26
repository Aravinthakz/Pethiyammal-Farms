package com.rmsvg.livestock.service;

import com.rmsvg.livestock.dto.PublicSettingsDto;
import com.rmsvg.livestock.entity.AppSettings;
import com.rmsvg.livestock.repository.AppSettingsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SettingsService {

    private final AppSettingsRepository repository;

    public SettingsService(AppSettingsRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public PublicSettingsDto publicSettings() {
        AppSettings s = repository.findById(1L).orElseGet(AppSettings::new);
        return new PublicSettingsDto(s.getWhatsappNumber(), s.getPhone(), s.getAddress(), s.getHours(), s.getMapEmbedUrl());
    }

    @Transactional
    public PublicSettingsDto update(PublicSettingsDto dto) {
        AppSettings s = repository.findById(1L).orElseGet(() -> {
            AppSettings n = new AppSettings();
            n.setId(1L);
            return n;
        });
        s.setWhatsappNumber(dto.whatsappNumber());
        s.setPhone(dto.phone());
        s.setAddress(dto.address());
        s.setHours(dto.hours());
        s.setMapEmbedUrl(dto.mapEmbedUrl());
        repository.save(s);
        return publicSettings();
    }
}
