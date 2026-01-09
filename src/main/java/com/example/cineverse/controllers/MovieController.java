package com.example.cineverse.controllers;

import com.example.cineverse.models.*;
import com.example.cineverse.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MovieController {
    private final MovieRepository movieRepository;
    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public MovieController(MovieRepository movieRepository, BookingRepository bookingRepository,
            ShowtimeRepository showtimeRepository, ReviewRepository reviewRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(@RequestParam(required = false) String search,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String sort,
            @org.springframework.web.bind.annotation.CookieValue(value = "city", required = false) String city,
            HttpSession session,
            Model model) {

        List<Movie> movies;

        // Apply search/filter
        if (search != null && !search.isEmpty()) {
            movies = movieRepository.findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCase(search, search);
            if (genre != null && !genre.isEmpty() && !"All".equals(genre)) {
                movies = movies.stream()
                        .filter(m -> m.getGenre().toLowerCase().contains(genre.toLowerCase()))
                        .collect(java.util.stream.Collectors.toList());
            }
        } else if (genre != null && !genre.isEmpty() && !"All".equals(genre)) {
            movies = movieRepository.findByGenreContainingIgnoreCase(genre);
        } else {
            movies = movieRepository.findAll();
        }

        // Apply sorting
        if (sort != null) {
            switch (sort) {
                case "rating":
                    movies.sort((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()));
                    break;
                case "price":
                    movies.sort((m1, m2) -> Double.compare(m1.getPrice(), m2.getPrice()));
                    break;
                case "popularity":
                    movies.sort((m1, m2) -> Integer.compare(m2.getPopularity(), m1.getPopularity()));
                    break;
                case "release":
                    movies.sort((m1, m2) -> m2.getReleaseDate().compareTo(m1.getReleaseDate()));
                    break;
            }
        }

        model.addAttribute("movies", movies.stream().filter(m -> !m.isUpcoming()).toList());
        model.addAttribute("upcomingMovies", movies.stream().filter(m -> m.isUpcoming()).toList());
        model.addAttribute("genres", java.util.Arrays.asList("Action", "Animation", "Comedy", "Crime", "Drama",
                "Horror", "Romance", "Sci-Fi", "Thriller"));

        // SMART RECOMMENDATIONS & WATCHLIST
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // Find genres from user bookings
            List<Booking> userBookings = bookingRepository.findByUser(user);
            if (!userBookings.isEmpty()) {
                // Get most frequent genre
                java.util.Map<String, Long> genreCounts = userBookings.stream()
                        .map(b -> b.getShowtime().getMovie().getGenre())
                        .collect(java.util.stream.Collectors.groupingBy(java.util.function.Function.identity(),
                                java.util.stream.Collectors.counting()));

                String favoriteGenre = genreCounts.entrySet().stream().max(java.util.Map.Entry.comparingByValue())
                        .map(java.util.Map.Entry::getKey).orElse(null);

                if (favoriteGenre != null) {
                    List<Movie> recommendations = movieRepository.findByGenreContainingIgnoreCase(favoriteGenre)
                            .stream()
                            .filter(m -> !m.isUpcoming())
                            .filter(m -> userBookings.stream()
                                    .noneMatch(b -> b.getShowtime().getMovie().getId().equals(m.getId()))) // Exclude
                                                                                                           // watched
                            .limit(4)
                            .toList();
                    if (!recommendations.isEmpty()) {
                        model.addAttribute("recommendations", recommendations);
                        model.addAttribute("recommendedGenre", favoriteGenre);
                    }
                }
            }

            // Pass user watchlist to model to check if movie is in watchlist (for heart
            // icon)
            // But watchlist is lazy/eager loaded in User. Let's make sure we have IDs.
            // Actually, we can just pass the user object (refreshed) or the existing
            // session user if updated.
            // Ideally, we refresh user here.
            user = userRepository.findById(user.getId()).orElse(user);
            if (user.getWatchlist() != null) {
                model.addAttribute("userWatchlist", user.getWatchlist().stream().map(Movie::getId).toList());
            } else {
                model.addAttribute("userWatchlist", java.util.Collections.emptyList());
            }
        }

        return "index";
    }

    @GetMapping("/watchlist")
    public String viewWatchlist(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        // Refresh user from DB to get latest watchlist
        user = userRepository.findById(user.getId()).orElse(user);
        model.addAttribute("watchlist", user.getWatchlist());
        return "watchlist";
    }

    @GetMapping("/watchlist/add/{movieId}")
    public String addToWatchlist(@PathVariable Long movieId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie != null) {
            user = userRepository.findById(user.getId()).orElse(user);
            user.getWatchlist().add(movie);
            userRepository.save(user);
            // Update session user
            session.setAttribute("user", user);
        }
        return "redirect:/watchlist";
    }

    @GetMapping("/watchlist/remove/{movieId}")
    public String removeFromWatchlist(@PathVariable Long movieId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie != null) {
            user = userRepository.findById(user.getId()).orElse(user);
            user.getWatchlist().remove(movie);
            userRepository.save(user);
            session.setAttribute("user", user);
        }
        return "redirect:/watchlist";
    }

    @GetMapping("/my-bookings")
    public String myBookings(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";
        model.addAttribute("bookings", bookingRepository.findByUser(user));
        return "my-bookings";
    }

    @GetMapping("/movie/{id}")
    public String movieDetails(@PathVariable Long id,
            @org.springframework.web.bind.annotation.CookieValue(value = "city", required = false) String city,
            Model model) {
        Movie movie = movieRepository.findById(id).orElseThrow();
        model.addAttribute("movie", movie);
        if (city == null || city.trim().isEmpty()) {
            model.addAttribute("showtimes", List.of());
        } else {
            List<Showtime> allShowtimes = showtimeRepository.findByMovie(movie);
            List<Showtime> filteredShowtimes = allShowtimes.stream()
                    .filter(s -> s.getTheater().getCity() != null && s.getTheater().getCity().equalsIgnoreCase(city))
                    .toList();
            model.addAttribute("showtimes", filteredShowtimes);
        }

        // Add reviews
        List<Review> reviews = reviewRepository.findByMovieAndModerationStatus(movie, "APPROVED");
        model.addAttribute("reviews", reviews);

        // Add average rating
        Double avgRating = reviewRepository.findAverageRatingByMovie(movie);
        model.addAttribute("averageRating", avgRating != null ? avgRating : 0.0);

        return "movie-details";
    }
}
