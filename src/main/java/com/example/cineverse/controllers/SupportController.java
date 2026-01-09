package com.example.cineverse.controllers;

import com.example.cineverse.models.SupportTicket;
import com.example.cineverse.models.User;
import com.example.cineverse.repositories.SupportTicketRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/support")
public class SupportController {

    @Autowired
    private SupportTicketRepository ticketRepo;

    @PostMapping("/create")
    public ResponseEntity<?> createTicket(@RequestBody Map<String, String> payload, HttpSession session) {
        String subject = payload.get("subject");
        String message = payload.get("message");

        // Try getting email from session first, otherwise try payload (if guest)
        User user = (User) session.getAttribute("user");
        String email = (user != null) ? user.getEmail() : payload.getOrDefault("email", "Guest");

        if (subject == null || subject.isEmpty() || message == null || message.isEmpty()) {
            return ResponseEntity.badRequest().body("Subject and Message required.");
        }

        SupportTicket ticket = new SupportTicket(email, subject, message);
        ticketRepo.save(ticket);

        return ResponseEntity.ok(Map.of("message", "Ticket Created", "id", ticket.getId()));
    }
}
