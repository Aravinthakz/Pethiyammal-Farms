package com.rmsvg.livestock.repository;

import com.rmsvg.livestock.domain.Enums.EnquiryStatus;
import com.rmsvg.livestock.entity.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {
    long countByStatus(EnquiryStatus status);
    List<Enquiry> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
}
