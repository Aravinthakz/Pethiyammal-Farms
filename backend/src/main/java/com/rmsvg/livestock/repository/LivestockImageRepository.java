package com.rmsvg.livestock.repository;

import com.rmsvg.livestock.entity.LivestockImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivestockImageRepository extends JpaRepository<LivestockImage, Long> {
}
