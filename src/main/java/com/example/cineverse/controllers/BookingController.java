package com.example.cineverse.controllers;

import com.example.cineverse.models.*;
import com.example.cineverse.repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;

    public BookingController(BookingRepository b, ShowtimeRepository s, UserRepository u, SeatRepository sr) {
        this.bookingRepository = b;
        this.showtimeRepository = s;
        this.userRepository = u;
        this.seatRepository = sr;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Map<String, Object> payload, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return ResponseEntity.status(401).body(Map.of("message", "User not logged in"));

        double totalPrice = Double.valueOf(payload.get("totalPrice").toString());
        String seatNumbers = (String) payload.get("seatNumbers");
        Long showtimeId = Long.valueOf(payload.get("showtimeId").toString());

        Showtime showtime = showtimeRepository.findById(showtimeId).orElseThrow();

        // Create booking
        Booking booking = new Booking(user, showtime,
                (String) payload.get("customerName"), seatNumbers, totalPrice);
        bookingRepository.save(booking);

        // Mark seats as booked
        String[] seats = seatNumbers.split(", ");
        for (String seatNum : seats) {
            seatRepository.findByShowtimeAndSeatNumber(showtime, seatNum.trim())
                    .ifPresent(seat -> {
                        seat.setBooked(true);
                        seat.setBooking(booking);
                        seatRepository.save(seat);
                    });
        }

        // Award Loyalty Points (10 per seat)
        int pointsEarned = seats.length * 10;
        if (user.getPoints() == null)
            user.setPoints(0);
        user.setPoints(user.getPoints() + pointsEarned);
        userRepository.save(user);
        session.setAttribute("user", user);

        return ResponseEntity.ok(Map.of("message", "Success", "qrCode", booking.getQrCode()));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return ResponseEntity.status(401).build();
        Booking booking = bookingRepository.findById(id).get();
        if (!booking.getUser().getId().equals(user.getId()))
            return ResponseEntity.status(403).build();
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
        return ResponseEntity.ok(Map.of("message", "Cancelled", "refundAmount", booking.getTotalPrice()));
    }
}
