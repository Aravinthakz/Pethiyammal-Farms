package com.rmsvg.livestock.repository;

import com.rmsvg.livestock.entity.AppSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppSettingsRepository extends JpaRepository<AppSettings, Long> {
}
