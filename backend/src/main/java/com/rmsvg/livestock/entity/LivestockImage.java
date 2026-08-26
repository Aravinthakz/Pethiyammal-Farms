package com.rmsvg.livestock.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "livestock_images")
public class LivestockImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id")
    private Livestock livestock;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    private boolean primaryImage;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Livestock getLivestock() { return livestock; }
    public void setLivestock(Livestock livestock) { this.livestock = livestock; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public boolean isPrimaryImage() { return primaryImage; }
    public void setPrimaryImage(boolean primaryImage) { this.primaryImage = primaryImage; }
}
