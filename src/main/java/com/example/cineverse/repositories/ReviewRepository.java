package com.example.cineverse.repositories;

import com.example.cineverse.models.Review;
import com.example.cineverse.models.Movie;
import com.example.cineverse.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMovie(Movie movie);

    List<Review> findByMovieAndModerationStatus(Movie movie, String status);

    List<Review> findByUser(User user);

    Optional<Review> findByMovieAndUser(Movie movie, User user);

    List<Review> findByModerationStatus(String status);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.movie = ?1 AND r.moderationStatus = 'APPROVED'")
    Double findAverageRatingByMovie(Movie movie);
}
