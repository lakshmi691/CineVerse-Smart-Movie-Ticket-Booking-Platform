package com.example.cineverse.controllers;

import com.example.cineverse.models.*;
import com.example.cineverse.repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/seats")
public class SeatController {
    private final SeatRepository seatRepository;
    private final ShowtimeRepository showtimeRepository;

    public SeatController(SeatRepository seatRepository, ShowtimeRepository showtimeRepository) {
        this.seatRepository = seatRepository;
        this.showtimeRepository = showtimeRepository;
    }

    @GetMapping("/{showtimeId}")
    public ResponseEntity<?> getSeats(@PathVariable Long showtimeId) {
        Optional<Showtime> showtimeOpt = showtimeRepository.findById(showtimeId);
        if (showtimeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Showtime showtime = showtimeOpt.get();
        Theater theater = showtime.getTheater();

        if (theater == null) {
            return ResponseEntity.badRequest().body("No theater associated with showtime");
        }

        // Get all possible seats for this theater and ensure they exist in DB linked to
        // this showtime
        List<Seat> currentSeatsInDb = seatRepository.findByShowtimeId(showtimeId);
        Map<String, Seat> seatMap = currentSeatsInDb.stream()
                .collect(Collectors.toMap(Seat::getSeatNumber, s -> s));

        List<Seat> fullTheaterSeats = new ArrayList<>();

        // Helper to process rows/cols
        processSection(fullTheaterSeats, theater.getPremiumRows(), theater.getPremiumCols(), "P", "PREMIUM", showtime,
                seatMap);
        processSection(fullTheaterSeats, theater.getGoldRows(), theater.getGoldCols(), "G", "GOLD", showtime, seatMap);
        processSection(fullTheaterSeats, theater.getSilverRows(), theater.getSilverCols(), "S", "SILVER", showtime,
                seatMap);

        // Convert to DTO with clear property names
        List<Map<String, Object>> seatData = fullTheaterSeats.stream().map(seat -> {
            Map<String, Object> data = new HashMap<>();
            data.put("seatNumber", seat.getSeatNumber());
            data.put("seatType", seat.getSeatType());
            data.put("price", seat.getPrice());
            data.put("isBooked", seat.isBooked());
            return data;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("seats", seatData));
    }

    private void processSection(List<Seat> result, int rows, int cols, String prefix, String type, Showtime st,
            Map<String, Seat> existing) {
        double price = type.equals("PREMIUM") ? st.getPricePremium()
                : (type.equals("GOLD") ? st.getPriceGold() : st.getPriceSilver());
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                String num = prefix + r + "-" + c;
                if (existing.containsKey(num)) {
                    result.add(existing.get(num));
                } else {
                    Seat s = new Seat(num, type, st, price);
                    // Persist newly discovered available seats
                    seatRepository.save(s);
                    result.add(s);
                }
            }
        }
    }
}
