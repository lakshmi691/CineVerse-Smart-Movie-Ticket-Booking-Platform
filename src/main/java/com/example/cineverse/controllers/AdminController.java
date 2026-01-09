package com.example.cineverse.controllers;

import com.example.cineverse.models.*;
import com.example.cineverse.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import java.util.*;

@Controller
public class AdminController {
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;

    public AdminController(MovieRepository movieRepository, UserRepository userRepository,
            BookingRepository bookingRepository, ReviewRepository reviewRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
    }

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && "ADMIN".equals(user.getRole());
    }

    @GetMapping("/admin")
    public String adminDashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        // Statistics
        long totalUsers = userRepository.count();
        long totalBookings = bookingRepository.count();
        long totalMovies = movieRepository.count();
        long totalReviews = reviewRepository.count();

        // Calculate revenue
        List<Booking> allBookings = bookingRepository.findAll();
        double totalRevenue = allBookings.stream().mapToDouble(Booking::getTotalPrice).sum();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalMovies", totalMovies);
        model.addAttribute("totalReviews", totalReviews);

        // Recent bookings
        List<Booking> recentBookings = bookingRepository.findAll();
        if (recentBookings.size() > 10) {
            recentBookings = recentBookings.subList(recentBookings.size() - 10, recentBookings.size());
        }
        Collections.reverse(recentBookings);
        model.addAttribute("recentBookings", recentBookings);

        return "admin-dashboard";
    }

    @GetMapping("/admin/users")
    public String adminUsers(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("users", userRepository.findAll());
        return "admin-users";
    }

    @GetMapping("/admin/reviews")
    public String adminReviews(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("pendingReviews", reviewRepository.findByModerationStatus("PENDING"));
        model.addAttribute("allReviews", reviewRepository.findAll());
        return "admin-reviews";
    }

    @PostMapping("/admin/reviews/{id}/approve")
    @ResponseBody
    public ResponseEntity<?> approveReview(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return ResponseEntity.status(403).build();
        }
        Review review = reviewRepository.findById(id).orElseThrow();
        review.setModerationStatus("APPROVED");
        reviewRepository.save(review);
        return ResponseEntity.ok(Map.of("message", "Approved"));
    }

    @PostMapping("/admin/reviews/{id}/reject")
    @ResponseBody
    public ResponseEntity<?> rejectReview(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return ResponseEntity.status(403).build();
        }
        Review review = reviewRepository.findById(id).orElseThrow();
        review.setModerationStatus("REJECTED");
        reviewRepository.save(review);
        return ResponseEntity.ok(Map.of("message", "Rejected"));
    }
}
