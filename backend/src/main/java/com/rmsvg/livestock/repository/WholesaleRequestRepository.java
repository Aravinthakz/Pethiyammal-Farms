package com.rmsvg.livestock.repository;

import com.rmsvg.livestock.domain.Enums.WholesaleStatus;
import com.rmsvg.livestock.entity.WholesaleRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WholesaleRequestRepository extends JpaRepository<WholesaleRequest, Long> {
    long countByStatus(WholesaleStatus status);
}
