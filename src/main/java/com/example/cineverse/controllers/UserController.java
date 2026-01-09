package com.example.cineverse.controllers;

import com.example.cineverse.models.User;
import com.example.cineverse.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        // Refresh user from DB
        user = userRepository.findById(user.getId()).orElse(user);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/wallet")
    public String wallet(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        // Refresh user from DB
        user = userRepository.findById(user.getId()).orElse(user);
        model.addAttribute("user", user);
        return "wallet";
    }

    @PostMapping("/wallet/add")
    public String addMoney(@RequestParam double amount, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        user = userRepository.findById(user.getId()).orElse(user);
        user.setWalletBalance(user.getWalletBalance() + amount);
        userRepository.save(user);
        session.setAttribute("user", user);
        return "redirect:/wallet";
    }
}
