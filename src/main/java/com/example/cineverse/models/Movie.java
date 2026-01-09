package com.example.cineverse.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String genre;
    @Column(length = 1000)
    private String description;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "duration_min")
    private int durationMin;
    private double rating;
    private double price;
    private int voteCount;
    private int popularity;
    private LocalDate releaseDate;
    @Column(length = 500)
    private String trailerUrl; // YouTube Embed URL
    @Column(name = "is_upcoming")
    private Boolean upcoming = false;

    public Movie() {
    }

    public Movie(String title, String genre, String description, String imageUrl, int durationMin, double rating,
            double price, int popularity, boolean upcoming) {
        this.title = title;
        this.genre = genre;
        this.description = description;
        this.imageUrl = imageUrl;
        this.durationMin = durationMin;
        this.rating = rating;
        this.price = price;
        this.voteCount = (int) (Math.random() * 5000) + 100;
        this.popularity = popularity;
        this.upcoming = upcoming;
        if (upcoming) {
            this.releaseDate = LocalDate.now().plusDays((long) (Math.random() * 60) + 1);
        } else {
            this.releaseDate = LocalDate.now().minusDays((long) (Math.random() * 3000));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(int durationMin) {
        this.durationMin = durationMin;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public boolean isUpcoming() {
        return upcoming;
    }

    public void setUpcoming(Boolean upcoming) {
        this.upcoming = upcoming != null && upcoming;
    }
}
