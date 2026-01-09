package com.example.cineverse.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String code;
    private int discountPercentage;
    private LocalDate expiryDate;

    public Coupon() {
    }

    public Coupon(String code, int discountPercentage, LocalDate expiryDate) {
        this.code = code;
        this.discountPercentage = discountPercentage;
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }
}
