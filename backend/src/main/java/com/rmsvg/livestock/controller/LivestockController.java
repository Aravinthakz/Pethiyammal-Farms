package com.rmsvg.livestock.controller;

import com.rmsvg.livestock.domain.Enums.Category;
import com.rmsvg.livestock.domain.Enums.Gender;
import com.rmsvg.livestock.dto.LivestockDto;
import com.rmsvg.livestock.service.LivestockService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livestock")
public class LivestockController {

    private final LivestockService livestockService;

    public LivestockController(LivestockService livestockService) {
        this.livestockService = livestockService;
    }

    @GetMapping
    public List<LivestockDto> list(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) String age,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String sort
    ) {
        return livestockService.searchPublic(category, age, gender, price, sort);
    }

    @GetMapping("/featured")
    public List<LivestockDto> featured() {
        return livestockService.featured();
    }

    @GetMapping("/{id}")
    public LivestockDto get(@PathVariable Long id) {
        return livestockService.getPublic(id);
    }

    @GetMapping("/{id}/similar")
    public List<LivestockDto> similar(@PathVariable Long id) {
        return livestockService.similar(id);
    }
}
