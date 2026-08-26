package com.rmsvg.livestock.entity;

import com.rmsvg.livestock.domain.Enums.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "livestock")
public class Livestock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String animalCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Category category;

    @Column(nullable = false, length = 120)
    private String breed;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Gender gender;

    @Column(length = 60)
    private String ageLabel;

    private Integer ageMonths;

    @Column(precision = 10, scale = 2)
    private BigDecimal weightKg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PricingType pricingType = PricingType.FIXED;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(precision = 10, scale = 2)
    private BigDecimal minOrderQty;

    @Column(precision = 10, scale = 2)
    private BigDecimal availableQty;

    @Column(length = 120)
    private String location;

    @Column(length = 4000)
    private String description;

    @Column(length = 2000)
    private String whyChoose;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LivestockStatus status = LivestockStatus.AVAILABLE;

    private boolean featured;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "livestock", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("primaryImage DESC, id ASC")
    private List<LivestockImage> images = new ArrayList<>();

    @PreUpdate
    public void touch() {
        this.updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAnimalCode() { return animalCode; }
    public void setAnimalCode(String animalCode) { this.animalCode = animalCode; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public String getAgeLabel() { return ageLabel; }
    public void setAgeLabel(String ageLabel) { this.ageLabel = ageLabel; }
    public Integer getAgeMonths() { return ageMonths; }
    public void setAgeMonths(Integer ageMonths) { this.ageMonths = ageMonths; }
    public BigDecimal getWeightKg() { return weightKg; }
    public void setWeightKg(BigDecimal weightKg) { this.weightKg = weightKg; }
    public PricingType getPricingType() { return pricingType; }
    public void setPricingType(PricingType pricingType) { this.pricingType = pricingType; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getMinOrderQty() { return minOrderQty; }
    public void setMinOrderQty(BigDecimal minOrderQty) { this.minOrderQty = minOrderQty; }
    public BigDecimal getAvailableQty() { return availableQty; }
    public void setAvailableQty(BigDecimal availableQty) { this.availableQty = availableQty; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getWhyChoose() { return whyChoose; }
    public void setWhyChoose(String whyChoose) { this.whyChoose = whyChoose; }
    public LivestockStatus getStatus() { return status; }
    public void setStatus(LivestockStatus status) { this.status = status; }
    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public List<LivestockImage> getImages() { return images; }
    public void setImages(List<LivestockImage> images) { this.images = images; }
}
