package com.example.cineverse.controllers;

import com.example.cineverse.models.Theater;
import com.example.cineverse.repositories.TheaterRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/theaters")
public class AdminTheaterController {

    private final TheaterRepository theaterRepository;

    public AdminTheaterController(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    @GetMapping
    public String listTheaters(Model model) {
        model.addAttribute("theaters", theaterRepository.findAll());
        return "admin-theaters";
    }

    @PostMapping("/add")
    public String addTheater(@RequestParam String name, @RequestParam String city, @RequestParam String address,
            @RequestParam int rows, @RequestParam int cols) {
        // Simplified Seat Layout: Equal rows/cols for all sections for now
        // UI can be enhanced later for per-section configuration
        Theater theater = new Theater(name, city, address, rows, cols, rows, cols, rows, cols);
        theaterRepository.save(theater);
        return "redirect:/admin/theaters";
    }

    @GetMapping("/edit/{id}")
    public String editTheater(@PathVariable Long id, Model model) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid theater Id:" + id));
        model.addAttribute("theater", theater);
        return "admin-theater-edit";
    }

    @PostMapping("/update")
    public String updateTheater(@RequestParam Long id, @RequestParam String name, @RequestParam String city,
            @RequestParam String address) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid theater Id:" + id));
        theater.setName(name);
        theater.setCity(city);
        theater.setAddress(address);
        theaterRepository.save(theater);
        return "redirect:/admin/theaters";
    }

    @PostMapping("/delete/{id}")
    public String deleteTheater(@PathVariable Long id) {
        if (id != null) {
            theaterRepository.deleteById(id);
        }
        return "redirect:/admin/theaters";
    }
}
