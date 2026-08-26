package com.rmsvg.livestock.service;

import com.rmsvg.livestock.domain.Enums.EnquiryStatus;
import com.rmsvg.livestock.domain.Enums.PricingType;
import com.rmsvg.livestock.dto.EnquiryRequest;
import com.rmsvg.livestock.entity.Customer;
import com.rmsvg.livestock.entity.Enquiry;
import com.rmsvg.livestock.entity.Livestock;
import com.rmsvg.livestock.exception.ApiException;
import com.rmsvg.livestock.repository.CustomerRepository;
import com.rmsvg.livestock.repository.EnquiryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EnquiryService {

    private final EnquiryRepository enquiryRepository;
    private final CustomerRepository customerRepository;
    private final LivestockService livestockService;

    public EnquiryService(EnquiryRepository enquiryRepository,
                          CustomerRepository customerRepository,
                          LivestockService livestockService) {
        this.enquiryRepository = enquiryRepository;
        this.customerRepository = customerRepository;
        this.livestockService = livestockService;
    }

    @Transactional
    public Map<String, Object> create(EnquiryRequest req) {
        Livestock livestock = null;
        if (req.livestockId() != null) {
            livestock = livestockService.getEntity(req.livestockId());
            if (livestock.getPricingType() == PricingType.PER_KG) {
                BigDecimal qty = req.quantity();
                if (livestock.getMinOrderQty() != null && qty.compareTo(livestock.getMinOrderQty()) < 0) {
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Quantity is below the minimum order");
                }
                if (livestock.getAvailableQty() != null && qty.compareTo(livestock.getAvailableQty()) > 0) {
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Not enough stock available");
                }
            }
        }
        Customer customer = customerRepository.findByPhone(req.phone()).orElseGet(Customer::new);
        customer.setName(req.fullName());
        customer.setPhone(req.phone());
        customer.setLocation(req.deliveryLocation());
        customer = customerRepository.save(customer);

        Enquiry enquiry = new Enquiry();
        enquiry.setCustomer(customer);
        enquiry.setLivestock(livestock);
        enquiry.setProductSnapshot(req.productSelection() != null ? req.productSelection() :
                (livestock != null ? livestock.getBreed() + " (" + livestock.getAnimalCode() + ")" : "General"));
        enquiry.setQuantity(req.quantity());
        enquiry.setPreferredDate(req.preferredDate());
        enquiry.setDeliveryLocation(req.deliveryLocation());
        enquiry.setMessage(req.message());
        enquiry = enquiryRepository.save(enquiry);

        Map<String, Object> res = new HashMap<>();
        res.put("id", enquiry.getId());
        res.put("status", enquiry.getStatus());
        res.put("message", "Enquiry submitted");
        return res;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> list() {
        return enquiryRepository.findAll().stream().map(this::toMap).toList();
    }

    @Transactional
    public Map<String, Object> updateStatus(Long id, EnquiryStatus status, String notes) {
        Enquiry e = enquiryRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Enquiry not found"));
        e.setStatus(status);
        if (notes != null) e.setNotes(notes);
        return toMap(enquiryRepository.save(e));
    }

    public List<Enquiry> byCustomer(Long customerId) {
        return enquiryRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    public Map<String, Object> toMap(Enquiry e) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", e.getId());
        m.put("status", e.getStatus());
        m.put("product", e.getProductSnapshot());
        m.put("quantity", e.getQuantity());
        m.put("preferredDate", e.getPreferredDate());
        m.put("deliveryLocation", e.getDeliveryLocation());
        m.put("message", e.getMessage());
        m.put("notes", e.getNotes());
        m.put("createdAt", e.getCreatedAt());
        if (e.getCustomer() != null) {
            m.put("customerName", e.getCustomer().getName());
            m.put("phone", e.getCustomer().getPhone());
            m.put("customerId", e.getCustomer().getId());
        }
        if (e.getLivestock() != null) {
            m.put("livestockId", e.getLivestock().getId());
            m.put("animalCode", e.getLivestock().getAnimalCode());
        }
        return m;
    }
}
