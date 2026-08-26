package com.rmsvg.livestock.domain;

public final class Enums {
    private Enums() {}

    public enum Role { ADMIN, STAFF }

    public enum Category { GOAT, COW, CHICKEN }

    public enum Gender { MALE, FEMALE }

    public enum PricingType { FIXED, PER_KG }

    public enum LivestockStatus { AVAILABLE, RESERVED, SOLD, INACTIVE }

    public enum EnquiryStatus { NEW, CONTACTED, INTERESTED, CONFIRMED, NOT_INTERESTED, CLOSED }

    public enum OrderStatus {
        PENDING, CONTACTED, CONFIRMED, PAYMENT_RECEIVED, READY, DELIVERED, COMPLETED, CANCELLED
    }

    public enum WholesaleStatus { NEW, CONTACTED, QUOTED, CONFIRMED, CLOSED }
}
