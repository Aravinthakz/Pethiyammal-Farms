package com.rmsvg.livestock.repository;

import com.rmsvg.livestock.domain.Enums.Category;
import com.rmsvg.livestock.domain.Enums.LivestockStatus;
import com.rmsvg.livestock.entity.Livestock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface LivestockRepository extends JpaRepository<Livestock, Long>, JpaSpecificationExecutor<Livestock> {
    Optional<Livestock> findByAnimalCode(String animalCode);
    boolean existsByAnimalCode(String animalCode);
    List<Livestock> findByFeaturedTrueAndStatus(LivestockStatus status);
    List<Livestock> findByCategoryAndStatusAndIdNot(Category category, LivestockStatus status, Long id);
    long countByStatus(LivestockStatus status);
}
