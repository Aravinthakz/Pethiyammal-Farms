package com.rmsvg.livestock.controller;

import com.rmsvg.livestock.dto.EnquiryRequest;
import com.rmsvg.livestock.dto.WholesaleRequestDto;
import com.rmsvg.livestock.service.EnquiryService;
import com.rmsvg.livestock.service.WholesaleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PublicFormController {

    private final EnquiryService enquiryService;
    private final WholesaleService wholesaleService;

    public PublicFormController(EnquiryService enquiryService, WholesaleService wholesaleService) {
        this.enquiryService = enquiryService;
        this.wholesaleService = wholesaleService;
    }

    @PostMapping("/api/enquiries")
    public Map<String, Object> enquiry(@Valid @RequestBody EnquiryRequest request) {
        return enquiryService.create(request);
    }

    @PostMapping("/api/wholesale")
    public Map<String, Object> wholesale(@Valid @RequestBody WholesaleRequestDto request) {
        return wholesaleService.create(request);
    }
}
