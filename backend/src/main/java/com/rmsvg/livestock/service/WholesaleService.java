package com.rmsvg.livestock.service;

import com.rmsvg.livestock.domain.Enums.WholesaleStatus;
import com.rmsvg.livestock.dto.WholesaleRequestDto;
import com.rmsvg.livestock.entity.Customer;
import com.rmsvg.livestock.entity.WholesaleRequest;
import com.rmsvg.livestock.exception.ApiException;
import com.rmsvg.livestock.repository.CustomerRepository;
import com.rmsvg.livestock.repository.WholesaleRequestRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WholesaleService {

    private final WholesaleRequestRepository repository;
    private final CustomerRepository customerRepository;

    public WholesaleService(WholesaleRequestRepository repository, CustomerRepository customerRepository) {
        this.repository = repository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Map<String, Object> create(WholesaleRequestDto req) {
        Customer customer = customerRepository.findByPhone(req.phone()).orElseGet(Customer::new);
        customer.setName(req.contactName());
        customer.setPhone(req.phone());
        customer.setLocation(req.location());
        customerRepository.save(customer);

        WholesaleRequest wr = new WholesaleRequest();
        wr.setBusinessName(req.businessName());
        wr.setContactName(req.contactName());
        wr.setPhone(req.phone());
        wr.setProduct(req.product());
        wr.setQuantity(req.quantity());
        wr.setBudget(req.budget());
        wr.setLocation(req.location());
        wr.setRequiredDate(req.requiredDate());
        wr.setMessage(req.message());
        wr = repository.save(wr);

        Map<String, Object> res = new HashMap<>();
        res.put("id", wr.getId());
        res.put("status", wr.getStatus());
        res.put("message", "Wholesale request submitted");
        return res;
    }

    @Transactional(readOnly = true)
    public List<WholesaleRequest> list() {
        return repository.findAll();
    }

    @Transactional
    public WholesaleRequest updateStatus(Long id, WholesaleStatus status) {
        WholesaleRequest wr = repository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Wholesale request not found"));
        wr.setStatus(status);
        return repository.save(wr);
    }
}
