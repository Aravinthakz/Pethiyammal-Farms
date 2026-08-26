package com.rmsvg.livestock.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "settings")
public class AppSettings {

    @Id
    private Long id = 1L;

    @Column(length = 20)
    private String whatsappNumber;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String address;

    @Column(length = 120)
    private String hours;

    @Column(length = 1000)
    private String mapEmbedUrl;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getWhatsappNumber() { return whatsappNumber; }
    public void setWhatsappNumber(String whatsappNumber) { this.whatsappNumber = whatsappNumber; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getHours() { return hours; }
    public void setHours(String hours) { this.hours = hours; }
    public String getMapEmbedUrl() { return mapEmbedUrl; }
    public void setMapEmbedUrl(String mapEmbedUrl) { this.mapEmbedUrl = mapEmbedUrl; }
}
