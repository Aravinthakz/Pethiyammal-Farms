package com.rmsvg.livestock.controller;

import com.rmsvg.livestock.dto.PublicSettingsDto;
import com.rmsvg.livestock.service.SettingsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
public class PublicSettingsController {

    private final SettingsService settingsService;

    public PublicSettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping("/public")
    public PublicSettingsDto publicSettings() {
        return settingsService.publicSettings();
    }
}
