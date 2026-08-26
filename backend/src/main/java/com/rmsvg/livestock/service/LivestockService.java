package com.rmsvg.livestock.service;

import com.rmsvg.livestock.domain.Enums.*;
import com.rmsvg.livestock.dto.LivestockDto;
import com.rmsvg.livestock.dto.LivestockRequest;
import com.rmsvg.livestock.entity.Livestock;
import com.rmsvg.livestock.entity.LivestockImage;
import com.rmsvg.livestock.exception.ApiException;
import com.rmsvg.livestock.repository.LivestockImageRepository;
import com.rmsvg.livestock.repository.LivestockRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class LivestockService {

    private final LivestockRepository livestockRepository;
    private final LivestockImageRepository imageRepository;
    private final FileStorageService files;

    public LivestockService(LivestockRepository livestockRepository,
                            LivestockImageRepository imageRepository,
                            FileStorageService files) {
        this.livestockRepository = livestockRepository;
        this.imageRepository = imageRepository;
        this.files = files;
    }

    @Transactional(readOnly = true)
    public List<LivestockDto> searchPublic(Category category, String age, Gender gender, String price, String sort) {
        Specification<Livestock> spec = (root, query, cb) -> {
            List<Predicate> preds = new ArrayList<>();
            preds.add(cb.equal(root.get("status"), LivestockStatus.AVAILABLE));
            if (category != null) {
                preds.add(cb.equal(root.get("category"), category));
            }
            if (gender != null) {
                preds.add(cb.equal(root.get("gender"), gender));
            }
            if (age != null && !age.isBlank()) {
                switch (age) {
                    case "0-6" -> preds.add(cb.between(root.get("ageMonths"), 0, 6));
                    case "6-12" -> preds.add(cb.between(root.get("ageMonths"), 6, 12));
                    case "12-24" -> preds.add(cb.between(root.get("ageMonths"), 12, 24));
                    case "24+" -> preds.add(cb.greaterThan(root.get("ageMonths"), 24));
                    default -> { }
                }
            }
            if (price != null && !price.isBlank()) {
                switch (price) {
                    case "under-10000" -> preds.add(cb.lt(root.get("price"), new BigDecimal("10000")));
                    case "10000-25000" -> preds.add(cb.between(root.get("price"), new BigDecimal("10000"), new BigDecimal("25000")));
                    case "25000-50000" -> preds.add(cb.between(root.get("price"), new BigDecimal("25000"), new BigDecimal("50000")));
                    case "50000+" -> preds.add(cb.gt(root.get("price"), new BigDecimal("50000")));
                    default -> { }
                }
            }
            return cb.and(preds.toArray(Predicate[]::new));
        };
        List<Livestock> list = livestockRepository.findAll(spec);
        if ("price-asc".equals(sort)) {
            list.sort((a, b) -> a.getPrice().compareTo(b.getPrice()));
        } else if ("price-desc".equals(sort)) {
            list.sort((a, b) -> b.getPrice().compareTo(a.getPrice()));
        } else {
            list.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        }
        return list.stream().map(LivestockDto::from).toList();
    }

    @Transactional(readOnly = true)
    public List<LivestockDto> featured() {
        return livestockRepository.findByFeaturedTrueAndStatus(LivestockStatus.AVAILABLE)
                .stream().map(LivestockDto::from).toList();
    }

    @Transactional(readOnly = true)
    public LivestockDto getPublic(Long id) {
        Livestock l = livestockRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Livestock not found"));
        return LivestockDto.from(l);
    }

    @Transactional(readOnly = true)
    public List<LivestockDto> similar(Long id) {
        Livestock l = livestockRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Livestock not found"));
        return livestockRepository.findByCategoryAndStatusAndIdNot(l.getCategory(), LivestockStatus.AVAILABLE, id)
                .stream().limit(3).map(LivestockDto::from).toList();
    }

    @Transactional(readOnly = true)
    public List<LivestockDto> adminList(Category category, LivestockStatus status) {
        Specification<Livestock> spec = (root, query, cb) -> {
            List<Predicate> preds = new ArrayList<>();
            preds.add(cb.notEqual(root.get("status"), LivestockStatus.INACTIVE));
            if (category != null) preds.add(cb.equal(root.get("category"), category));
            if (status != null) preds.add(cb.equal(root.get("status"), status));
            return cb.and(preds.toArray(Predicate[]::new));
        };
        return livestockRepository.findAll(spec).stream().map(LivestockDto::from).toList();
    }

    @Transactional
    public LivestockDto create(LivestockRequest req) {
        if (livestockRepository.existsByAnimalCode(req.animalCode())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Animal ID already exists");
        }
        Livestock l = new Livestock();
        apply(l, req);
        return LivestockDto.from(livestockRepository.save(l));
    }

    @Transactional
    public LivestockDto update(Long id, LivestockRequest req) {
        Livestock l = getEntity(id);
        livestockRepository.findByAnimalCode(req.animalCode()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Animal ID already exists");
            }
        });
        apply(l, req);
        return LivestockDto.from(livestockRepository.save(l));
    }

    @Transactional
    public void softDelete(Long id) {
        Livestock l = getEntity(id);
        l.setStatus(LivestockStatus.INACTIVE);
        l.setFeatured(false);
        livestockRepository.save(l);
    }

    @Transactional
    public LivestockDto updateStatus(Long id, LivestockStatus status) {
        Livestock l = getEntity(id);
        l.setStatus(status);
        return LivestockDto.from(livestockRepository.save(l));
    }

    @Transactional
    public LivestockDto updatePrice(Long id, BigDecimal price) {
        Livestock l = getEntity(id);
        l.setPrice(price);
        return LivestockDto.from(livestockRepository.save(l));
    }

    @Transactional
    public LivestockDto addImage(Long id, MultipartFile file, boolean primary) {
        Livestock l = getEntity(id);
        LivestockImage img = new LivestockImage();
        img.setLivestock(l);
        img.setImageUrl(files.store(file));
        img.setPrimaryImage(primary || l.getImages().isEmpty());
        if (img.isPrimaryImage()) {
            l.getImages().forEach(i -> i.setPrimaryImage(false));
        }
        l.getImages().add(img);
        livestockRepository.save(l);
        return LivestockDto.from(l);
    }

    @Transactional
    public LivestockDto deleteImage(Long livestockId, Long imageId) {
        Livestock l = getEntity(livestockId);
        LivestockImage img = l.getImages().stream()
                .filter(i -> i.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Image not found"));
        l.getImages().remove(img);
        imageRepository.delete(img);
        return LivestockDto.from(l);
    }

    public Livestock getEntity(Long id) {
        return livestockRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Livestock not found"));
    }

    private void apply(Livestock l, LivestockRequest req) {
        l.setAnimalCode(req.animalCode());
        l.setCategory(req.category());
        l.setBreed(req.breed());
        l.setGender(req.gender());
        l.setAgeLabel(req.ageLabel());
        l.setAgeMonths(req.ageMonths());
        l.setWeightKg(req.weightKg());
        l.setPricingType(req.pricingType());
        l.setPrice(req.price());
        l.setMinOrderQty(req.minOrderQty());
        l.setAvailableQty(req.availableQty());
        l.setLocation(req.location());
        l.setDescription(req.description());
        l.setWhyChoose(req.whyChoose());
        if (req.status() != null) l.setStatus(req.status());
        if (req.featured() != null) l.setFeatured(req.featured());
    }
}
