package com.example.cineverse.controllers;

import com.example.cineverse.models.*;
import com.example.cineverse.repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public ReviewController(ReviewRepository reviewRepository, MovieRepository movieRepository,
            UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Map<String, Object> payload, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return ResponseEntity.status(401).body(Map.of("message", "User not logged in"));
        }

        // Re-fetch user to ensure entity is managed by Hibernate
        Long userId = sessionUser.getId();
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("message", "User ID is missing from session"));
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "User session invalid"));
        }

        Object rawMovieId = payload.get("movieId");
        if (rawMovieId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Movie ID is missing"));
        }
        Long movieId = Long.valueOf(rawMovieId.toString());
        int rating = Integer.parseInt(payload.get("rating").toString());
        String reviewText = (String) payload.get("reviewText");

        Movie movie = movieRepository.findById(movieId)
                .orElse(null);
        if (movie == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Movie not found with ID: " + movieId));
        }

        // Check if user already reviewed this movie
        Optional<Review> existingReview = reviewRepository.findByMovieAndUser(movie, user);
        if (existingReview.isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "You have already reviewed this movie"));
        }

        // Create review
        Review review = new Review(user, movie, rating, reviewText);
        review.setModerationStatus("APPROVED"); // Auto-approve for now
        reviewRepository.save(review);

        return ResponseEntity.ok(Map.of("message", "Review submitted successfully", "reviewId", review.getId()));
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<?> getMovieReviews(@PathVariable Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow();
        List<Review> reviews = reviewRepository.findByMovieAndModerationStatus(movie, "APPROVED");

        List<Map<String, Object>> reviewData = new ArrayList<>();
        for (Review review : reviews) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", review.getId());
            data.put("userName", review.getUser().getName());
            data.put("rating", review.getRating());
            data.put("reviewText", review.getReviewText());
            data.put("createdAt", review.getCreatedAt().toString());
            data.put("helpfulCount", review.getHelpfulCount());
            data.put("unhelpfulCount", review.getUnhelpfulCount());
            reviewData.add(data);
        }

        Double avgRating = reviewRepository.findAverageRatingByMovie(movie);

        return ResponseEntity.ok(Map.of(
                "reviews", reviewData,
                "averageRating", avgRating != null ? avgRating : 0.0,
                "totalReviews", reviews.size()));
    }

    @PostMapping("/{id}/helpful")
    public ResponseEntity<?> markHelpful(@PathVariable Long id) {
        Review review = reviewRepository.findById(id).orElseThrow();
        review.setHelpfulCount(review.getHelpfulCount() + 1);
        reviewRepository.save(review);
        return ResponseEntity.ok(Map.of("helpfulCount", review.getHelpfulCount()));
    }

    @PostMapping("/{id}/unhelpful")
    public ResponseEntity<?> markUnhelpful(@PathVariable Long id) {
        Review review = reviewRepository.findById(id).orElseThrow();
        review.setUnhelpfulCount(review.getUnhelpfulCount() + 1);
        reviewRepository.save(review);
        return ResponseEntity.ok(Map.of("unhelpfulCount", review.getUnhelpfulCount()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        Review review = reviewRepository.findById(id).orElseThrow();

        // Check if user owns the review or is admin
        if (!review.getUser().getId().equals(user.getId()) && !"ADMIN".equals(user.getRole())) {
            return ResponseEntity.status(403).build();
        }

        reviewRepository.delete(review);
        return ResponseEntity.ok(Map.of("message", "Review deleted"));
    }
}
