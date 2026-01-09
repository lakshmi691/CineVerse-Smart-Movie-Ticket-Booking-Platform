package com.example.cineverse.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "movie_id" })
})
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(nullable = false)
    private int rating; // 1-5 stars

    @Column(length = 1000)
    private String reviewText;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "helpful_count")
    private int helpfulCount = 0;

    @Column(name = "unhelpful_count")
    private int unhelpfulCount = 0;

    @Column(name = "moderation_status")
    private String moderationStatus = "APPROVED"; // PENDING, APPROVED, REJECTED

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Review() {
    }

    public Review(User user, Movie movie, int rating, String reviewText) {
        this.user = user;
        this.movie = movie;
        this.rating = rating;
        this.reviewText = reviewText;
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

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getHelpfulCount() {
        return helpfulCount;
    }

    public void setHelpfulCount(int helpfulCount) {
        this.helpfulCount = helpfulCount;
    }

    public int getUnhelpfulCount() {
        return unhelpfulCount;
    }

    public void setUnhelpfulCount(int unhelpfulCount) {
        this.unhelpfulCount = unhelpfulCount;
    }

    public String getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(String moderationStatus) {
        this.moderationStatus = moderationStatus;
    }
}
