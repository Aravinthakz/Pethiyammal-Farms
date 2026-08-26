package com.rmsvg.livestock.controller;

import com.rmsvg.livestock.domain.Enums.Category;
import com.rmsvg.livestock.domain.Enums.LivestockStatus;
import com.rmsvg.livestock.dto.LivestockDto;
import com.rmsvg.livestock.dto.LivestockRequest;
import com.rmsvg.livestock.dto.PriceUpdateRequest;
import com.rmsvg.livestock.dto.StatusUpdateRequest;
import com.rmsvg.livestock.service.LivestockService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/livestock")
public class AdminLivestockController {

    private final LivestockService livestockService;

    public AdminLivestockController(LivestockService livestockService) {
        this.livestockService = livestockService;
    }

    @GetMapping
    public List<LivestockDto> list(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) LivestockStatus status
    ) {
        return livestockService.adminList(category, status);
    }

    @GetMapping("/{id}")
    public LivestockDto get(@PathVariable Long id) {
        return livestockService.getPublic(id);
    }

    @PostMapping
    public LivestockDto create(@Valid @RequestBody LivestockRequest request) {
        return livestockService.create(request);
    }

    @PutMapping("/{id}")
    public LivestockDto update(@PathVariable Long id, @Valid @RequestBody LivestockRequest request) {
        return livestockService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        livestockService.softDelete(id);
    }

    @PatchMapping("/{id}/status")
    public LivestockDto status(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        return livestockService.updateStatus(id, LivestockStatus.valueOf(request.status().toUpperCase()));
    }

    @PatchMapping("/{id}/price")
    public LivestockDto price(@PathVariable Long id, @Valid @RequestBody PriceUpdateRequest request) {
        return livestockService.updatePrice(id, request.price());
    }

    @PostMapping("/{id}/images")
    public LivestockDto upload(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "false") boolean primary
    ) {
        return livestockService.addImage(id, file, primary);
    }

    @DeleteMapping("/{id}/images/{imageId}")
    public LivestockDto deleteImage(@PathVariable Long id, @PathVariable Long imageId) {
        return livestockService.deleteImage(id, imageId);
    }
}
