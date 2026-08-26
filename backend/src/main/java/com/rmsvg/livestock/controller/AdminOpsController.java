package com.rmsvg.livestock.controller;

import com.rmsvg.livestock.domain.Enums.EnquiryStatus;
import com.rmsvg.livestock.domain.Enums.OrderStatus;
import com.rmsvg.livestock.domain.Enums.WholesaleStatus;
import com.rmsvg.livestock.dto.OrderCreateRequest;
import com.rmsvg.livestock.dto.PublicSettingsDto;
import com.rmsvg.livestock.dto.StatusUpdateRequest;
import com.rmsvg.livestock.entity.WholesaleRequest;
import com.rmsvg.livestock.service.DashboardService;
import com.rmsvg.livestock.service.EnquiryService;
import com.rmsvg.livestock.service.OrderService;
import com.rmsvg.livestock.service.SettingsService;
import com.rmsvg.livestock.service.WholesaleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminOpsController {

    private final DashboardService dashboardService;
    private final EnquiryService enquiryService;
    private final WholesaleService wholesaleService;
    private final OrderService orderService;
    private final SettingsService settingsService;

    public AdminOpsController(DashboardService dashboardService,
                              EnquiryService enquiryService,
                              WholesaleService wholesaleService,
                              OrderService orderService,
                              SettingsService settingsService) {
        this.dashboardService = dashboardService;
        this.enquiryService = enquiryService;
        this.wholesaleService = wholesaleService;
        this.orderService = orderService;
        this.settingsService = settingsService;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return dashboardService.stats();
    }

    @GetMapping("/reports")
    public Map<String, Object> reports() {
        return dashboardService.reports();
    }

    @GetMapping("/enquiries")
    public List<Map<String, Object>> enquiries() {
        return enquiryService.list();
    }

    @PatchMapping("/enquiries/{id}")
    public Map<String, Object> enquiryStatus(@PathVariable Long id,
                                             @Valid @RequestBody StatusUpdateRequest request,
                                             @RequestParam(required = false) String notes) {
        return enquiryService.updateStatus(id, EnquiryStatus.valueOf(request.status().toUpperCase()), notes);
    }

    @GetMapping("/wholesale")
    public List<WholesaleRequest> wholesale() {
        return wholesaleService.list();
    }

    @PatchMapping("/wholesale/{id}")
    public WholesaleRequest wholesaleStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        return wholesaleService.updateStatus(id, WholesaleStatus.valueOf(request.status().toUpperCase()));
    }

    @GetMapping("/orders")
    public List<Map<String, Object>> orders(@RequestParam(required = false) OrderStatus status) {
        return orderService.list(status);
    }

    @GetMapping("/orders/{id}")
    public Map<String, Object> order(@PathVariable Long id) {
        return orderService.get(id);
    }

    @PostMapping("/orders")
    public Map<String, Object> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        return orderService.create(request);
    }

    @PutMapping("/orders/{id}/status")
    public Map<String, Object> orderStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        return orderService.updateStatus(id, OrderStatus.valueOf(request.status().toUpperCase()));
    }

    @GetMapping("/customers")
    public List<Map<String, Object>> customers() {
        return dashboardService.customers();
    }

    @GetMapping("/customers/{id}")
    public Map<String, Object> customer(@PathVariable Long id) {
        return dashboardService.customer(id);
    }

    @GetMapping("/settings")
    public PublicSettingsDto settings() {
        return settingsService.publicSettings();
    }

    @PutMapping("/settings")
    public PublicSettingsDto updateSettings(@RequestBody PublicSettingsDto dto) {
        return settingsService.update(dto);
    }
}
