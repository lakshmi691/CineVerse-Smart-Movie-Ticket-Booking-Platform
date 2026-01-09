package com.example.cineverse.repositories;

import com.example.cineverse.models.Seat;
import com.example.cineverse.models.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShowtime(Showtime showtime);

    List<Seat> findByShowtimeAndBooked(Showtime showtime, boolean booked);

    List<Seat> findByShowtimeId(Long showtimeId);

    Optional<Seat> findByShowtimeAndSeatNumber(Showtime showtime, String seatNumber);
}
