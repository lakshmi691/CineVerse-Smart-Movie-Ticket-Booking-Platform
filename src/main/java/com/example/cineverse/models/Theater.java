package com.example.cineverse.models;

import jakarta.persistence.*;

@Entity
@Table(name = "theaters")
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int totalSeats;

    @Column(name = "premium_rows")
    private int premiumRows;

    @Column(name = "premium_cols")
    private int premiumCols;

    @Column(name = "gold_rows")
    private int goldRows;

    @Column(name = "gold_cols")
    private int goldCols;

    @Column(name = "silver_rows")
    private int silverRows;

    @Column(name = "silver_cols")
    private int silverCols;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    public Theater() {
    }

    public Theater(String name, String city, String address, int premiumRows, int premiumCols, int goldRows,
            int goldCols, int silverRows,
            int silverCols) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.premiumRows = premiumRows;
        this.premiumCols = premiumCols;
        this.goldRows = goldRows;
        this.goldCols = goldCols;
        this.silverRows = silverRows;
        this.silverCols = silverCols;
        this.totalSeats = (premiumRows * premiumCols) + (goldRows * goldCols) + (silverRows * silverCols);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getPremiumRows() {
        return premiumRows;
    }

    public void setPremiumRows(int premiumRows) {
        this.premiumRows = premiumRows;
    }

    public int getPremiumCols() {
        return premiumCols;
    }

    public void setPremiumCols(int premiumCols) {
        this.premiumCols = premiumCols;
    }

    public int getGoldRows() {
        return goldRows;
    }

    public void setGoldRows(int goldRows) {
        this.goldRows = goldRows;
    }

    public int getGoldCols() {
        return goldCols;
    }

    public void setGoldCols(int goldCols) {
        this.goldCols = goldCols;
    }

    public int getSilverRows() {
        return silverRows;
    }

    public void setSilverRows(int silverRows) {
        this.silverRows = silverRows;
    }

    public int getSilverCols() {
        return silverCols;
    }

    public void setSilverCols(int silverCols) {
        this.silverCols = silverCols;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
