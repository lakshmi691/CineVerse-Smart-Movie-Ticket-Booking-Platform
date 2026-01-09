package com.example.cineverse.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;
    @Column(nullable = false)
    private String customerName;
    @Column(name = "seat_numbers", nullable = false)
    private String seatNumbers;
    private double totalPrice;
    private String status;
    private String qrCode;
    private LocalDateTime bookingTime;

    @PrePersist
    protected void onCreate() {
        bookingTime = LocalDateTime.now();
        this.status = "BOOKED";
        this.qrCode = "QR_" + System.currentTimeMillis();
    }

    public Booking() {
    }

    public Booking(User user, Showtime showtime, String customerName, String seatNumbers, double totalPrice) {
        this.user = user;
        this.showtime = showtime;
        this.customerName = customerName;
        this.seatNumbers = seatNumbers;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(String seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
