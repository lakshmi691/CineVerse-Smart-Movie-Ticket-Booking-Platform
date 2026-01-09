package com.example.cineverse.controllers;

import com.example.cineverse.models.User;
import com.example.cineverse.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;

@Controller
public class AuthController {
    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String name, @RequestParam String email, @RequestParam String password,
            @RequestParam String mobile, Model model) {
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email exists!");
            return "register";
        }
        userRepository.save(new User(name, email, password, mobile));
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, HttpSession session,
            Model model) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            // SPECIAL MESSAGE FOR UNFOUND USER
            model.addAttribute("error", "Account not found. Please create an account.");
            return "login";
        }
        if (userOpt.get().getPassword().equals(password)) {
            session.setAttribute("user", userOpt.get());
            if ("ADMIN".equals(userOpt.get().getRole())) {
                return "redirect:/admin";
            }
            return "redirect:/";
        }
        model.addAttribute("error", "Invalid credentials");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
