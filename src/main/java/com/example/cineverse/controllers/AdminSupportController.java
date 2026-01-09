package com.example.cineverse.controllers;

import com.example.cineverse.models.SupportTicket;
import com.example.cineverse.models.User;
import com.example.cineverse.repositories.SupportTicketRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/support")
public class AdminSupportController {

    @Autowired
    private SupportTicketRepository ticketRepo;

    @GetMapping
    public String viewSupportInbox(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/";
        }
        model.addAttribute("tickets", ticketRepo.findAllByOrderByCreatedAtDesc());
        return "admin-support";
    }

    @PostMapping("/close/{id}")
    public String closeTicket(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/";
        }

        ticketRepo.findById(id).ifPresent(ticket -> {
            ticket.setStatus("CLOSED");
            ticketRepo.save(ticket);
        });

        return "redirect:/admin/support";
    }
}
