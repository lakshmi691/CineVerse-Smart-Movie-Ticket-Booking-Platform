package com.example.cineverse.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalDataAdvice {

    @ModelAttribute
    public void addGlobalAttributes(@CookieValue(value = "city", required = false) String city, Model model) {
        if (city == null || city.trim().isEmpty()) {
            // Removed to prevent popup on every page
            // model.addAttribute("locationMissing", true);
        } else {
            model.addAttribute("locationMissing", false);
            model.addAttribute("currentCity", city);
        }
    }
}
