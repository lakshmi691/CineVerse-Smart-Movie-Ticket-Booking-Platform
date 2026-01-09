package com.example.cineverse.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "showtimes")
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
    @ManyToOne
    @JoinColumn(name = "theater_id")
    private Theater theater;
    private String screenName;
    private LocalDateTime startTime;
    private double priceSilver;
    private double priceGold;
    private double pricePremium;

    public Showtime() {
    }

    public Showtime(Movie movie, Theater theater, String screenName, LocalDateTime startTime, double priceSilver,
            double priceGold,
            double pricePremium) {
        this.movie = movie;
        this.theater = theater;
        this.screenName = screenName;
        this.startTime = startTime;
        this.priceSilver = priceSilver;
        this.priceGold = priceGold;
        this.pricePremium = pricePremium;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public double getPriceSilver() {
        return priceSilver;
    }

    public void setPriceSilver(double priceSilver) {
        this.priceSilver = priceSilver;
    }

    public double getPriceGold() {
        return priceGold;
    }

    public void setPriceGold(double priceGold) {
        this.priceGold = priceGold;
    }

    public double getPricePremium() {
        return pricePremium;
    }

    public void setPricePremium(double pricePremium) {
        this.pricePremium = pricePremium;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    public String getFormattedTime() {
        return startTime.format(DateTimeFormatter.ofPattern("dd MMM, hh:mm a"));
    }
}
