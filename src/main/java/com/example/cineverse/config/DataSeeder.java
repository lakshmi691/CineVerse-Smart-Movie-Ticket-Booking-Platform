package com.example.cineverse.config;

import com.example.cineverse.models.Movie;
import com.example.cineverse.models.Showtime;
import com.example.cineverse.models.Coupon;
import com.example.cineverse.models.Theater;
import com.example.cineverse.models.User;
import com.example.cineverse.models.Booking;
import com.example.cineverse.repositories.MovieRepository;
import com.example.cineverse.repositories.ShowtimeRepository;
import com.example.cineverse.repositories.CouponRepository;
import com.example.cineverse.repositories.BookingRepository;
import com.example.cineverse.repositories.TheaterRepository;
import com.example.cineverse.repositories.UserRepository;
import com.example.cineverse.repositories.SeatRepository;
import com.example.cineverse.repositories.ReviewRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Configuration
public class DataSeeder {

        @PersistenceContext
        private EntityManager entityManager;

        @Bean
        CommandLineRunner initDatabase(MovieRepository movieRepo, ShowtimeRepository showtimeRepo,
                        CouponRepository couponRepo, BookingRepository bookingRepo,
                        TheaterRepository theaterRepo, UserRepository userRepo,
                        SeatRepository seatRepo, ReviewRepository reviewRepo) {
                return args -> {
                        seatRepo.deleteAll();
                        bookingRepo.deleteAll();
                        reviewRepo.deleteAll();
                        showtimeRepo.deleteAll();
                        movieRepo.deleteAll();
                        couponRepo.deleteAll();
                        theaterRepo.deleteAll();
                        userRepo.deleteAll();

                        // Create Mass Theaters (60 theaters across 12 cities)
                        List<Theater> allTheaters = new ArrayList<>();
                        String[] cities = { "Mumbai", "Delhi", "Bangalore", "Hyderabad", "Ahmedabad", "Chennai",
                                        "Kolkata", "Surat", "Pune", "Jaipur", "Lucknow", "Kanpur" };
                        String[] areas = { "Downtown", "West End", "North Block", "South Plaza", "East Coast",
                                        "Central",
                                        "Uptown", "Suburbs", "Tech Park", "Heritage City" };
                        String[] brands = { "PVR", "Inox", "Cinepolis", "IMAX", "MovieMax", "Miraj" };

                        for (String city : cities) {
                                for (int i = 1; i <= 10; i++) {
                                        String brand = brands[(city.length() + i) % brands.length];
                                        String area = areas[(city.length() + i) % areas.length];
                                        String name = brand + " " + area + " " + city;
                                        String address = area + " Main Rd, Street " + i + ", " + city;

                                        // Randomize seats between ~100 to ~175
                                        java.util.Random rand = new java.util.Random();

                                        // Premium: 2-3 rows, 8-12 cols
                                        int pRows = 2 + rand.nextInt(2);
                                        int pCols = 8 + rand.nextInt(5);

                                        // Gold: 3-4 rows, 10-14 cols
                                        int gRows = 3 + rand.nextInt(2);
                                        int gCols = 10 + rand.nextInt(5);

                                        // Silver: 3-4 rows, 10-14 cols
                                        int sRows = 3 + rand.nextInt(2);
                                        int sCols = 10 + rand.nextInt(5);

                                        allTheaters.add(new Theater(name, city, address, pRows, pCols, gRows, gCols,
                                                        sRows, sCols));
                                }
                        }
                        theaterRepo.saveAll(allTheaters);

                        // Create Admin User
                        User admin = new User("Admin", "admin@cineverse.com", "admin123", "9876543210");
                        admin.setWalletBalance(10000.0);
                        admin.setRole("ADMIN");
                        userRepo.save(admin);

                        List<Movie> movies = new ArrayList<>();

                        movies.add(new Movie("Inception", "Sci-Fi", "Dream hijacking.",
                                        "https://upload.wikimedia.org/wikipedia/en/2/2e/Inception_%282010%29_theatrical_poster.jpg",
                                        148, 4.9, 450, 2000, false));
                        movies.get(movies.size() - 1).setTrailerUrl("https://www.youtube.com/embed/YoHD9XEInc0");

                        movies.add(new Movie("Avengers: Endgame", "Action", "Heroes assemble.",
                                        "https://upload.wikimedia.org/wikipedia/en/0/0d/Avengers_Endgame_poster.jpg",
                                        181, 4.8, 500, 1900, false));
                        movies.get(movies.size() - 1).setTrailerUrl("https://www.youtube.com/embed/TcMBFSGVi1c");

                        movies.add(new Movie("The Dark Knight", "Action", "Batman vs Joker.",
                                        "https://upload.wikimedia.org/wikipedia/en/1/1c/The_Dark_Knight_%282008_film%29.jpg",
                                        152, 4.9, 400, 1950, false));
                        movies.get(movies.size() - 1).setTrailerUrl("https://www.youtube.com/embed/EXeTwQWrcwY");

                        movies.add(new Movie("Titanic", "Romance", "Ship meets iceberg.",
                                        "https://upload.wikimedia.org/wikipedia/en/1/18/Titanic_%281997_film%29_poster.png",
                                        194, 4.7, 350, 1800, false));
                        movies.get(movies.size() - 1).setTrailerUrl("https://www.youtube.com/embed/f7O6m0XN_uE");

                        movies.add(new Movie("Avatar 2", "Sci-Fi", "Return to Pandora.",
                                        "https://upload.wikimedia.org/wikipedia/en/5/54/Avatar_The_Way_of_Water_poster.jpg",
                                        192, 4.8, 450, 1850, false));
                        movies.get(movies.size() - 1).setTrailerUrl("https://www.youtube.com/embed/d9MyW72ELq0");

                        movies.add(new Movie("Interstellar", "Sci-Fi", "Space travel.",
                                        "https://upload.wikimedia.org/wikipedia/en/b/bc/Interstellar_film_poster.jpg",
                                        169, 4.8, 400, 1700, false));
                        movies.get(movies.size() - 1).setTrailerUrl("https://www.youtube.com/embed/zSWdZVtXT7E");

                        movies.add(new Movie("Joker", "Drama", "Put on a happy face.",
                                        "https://upload.wikimedia.org/wikipedia/en/e/e1/Joker_%282019_film%29_poster.jpg",
                                        122, 4.6, 300, 1600, false));
                        movies.get(movies.size() - 1).setTrailerUrl("https://www.youtube.com/embed/zAGVQLHvwOY");

                        movies.add(new Movie("Spider-Man: NWH", "Action", "Multiverse chaos.",
                                        "https://upload.wikimedia.org/wikipedia/en/0/00/Spider-Man_No_Way_Home_poster.jpg",
                                        148, 4.7, 380, 1750, false));
                        movies.get(movies.size() - 1).setTrailerUrl("https://www.youtube.com/embed/JfVOs4VSpmA");

                        movies.add(new Movie("Parasite", "Thriller", "Class struggle.",
                                        "https://upload.wikimedia.org/wikipedia/en/5/53/Parasite_%282019_film%29.png",
                                        132, 4.6, 300, 1500, false));
                        movies.get(movies.size() - 1).setTrailerUrl("https://www.youtube.com/embed/5xH0HfJHsaY");

                        movies.add(new Movie("The Godfather", "Crime", "Offer he can't refuse.",
                                        "https://upload.wikimedia.org/wikipedia/en/1/1c/Godfather_ver1.jpg", 175, 4.9,
                                        350, 1980, false));
                        movies.get(movies.size() - 1).setTrailerUrl("https://www.youtube.com/embed/sY1S34973zA");

                        movies.add(new Movie("Moana", "Animation", "Ocean calls.",
                                        "https://upload.wikimedia.org/wikipedia/en/2/26/Moana_Teaser_Poster.jpg", 107,
                                        4.7, 300, 1600, false));

                        movies.add(new Movie("K.G.F: Chapter 2", "Action", "Sultan of Narachi.",
                                        "https://upload.wikimedia.org/wikipedia/en/d/d0/K.G.F_Chapter_2.jpg", 168, 4.9,
                                        400, 2500, false));
                        movies.add(new Movie("Kantara", "Thriller", "A Divine Blockbuster.",
                                        "https://upload.wikimedia.org/wikipedia/en/8/84/Kantara_poster.jpeg", 150, 4.9,
                                        350, 2400, false));

                        movies.add(new Movie("RRR", "Action/Drama", "Rise Roar Revolt.",
                                        "https://upload.wikimedia.org/wikipedia/en/d/d7/RRR_Poster.jpg", 187, 4.9, 400,
                                        2600, false));

                        movies.add(new Movie("Big Hero 6", "Animation", "Baymax.",
                                        "https://upload.wikimedia.org/wikipedia/en/4/4b/Big_Hero_6_%28film%29_poster.jpg",
                                        102, 4.8, 300, 1600, false));
                        movies.add(new Movie("Tangled", "Animation", "Best day ever.",
                                        "https://upload.wikimedia.org/wikipedia/en/a/a8/Tangled_poster.jpg", 100, 4.8,
                                        300, 1600, false));

                        movies.add(new Movie("Black Panther", "Action", "Wakanda Forever.",
                                        "https://upload.wikimedia.org/wikipedia/en/d/d6/Black_Panther_%28film%29_poster.jpg",
                                        125, 4.7, 350, 1600, false));
                        movies.add(new Movie("Top Gun: Maverick", "Action", "Feel the need for speed.",
                                        "https://upload.wikimedia.org/wikipedia/en/1/13/Top_Gun_Maverick_Poster.jpg",
                                        125, 4.8, 400, 1750, false));
                        movies.add(new Movie("Toy Story 4", "Animation", "To infinity...",
                                        "https://upload.wikimedia.org/wikipedia/en/4/4c/Toy_Story_4_poster.jpg", 100,
                                        4.6, 300, 1500, false));
                        movies.add(new Movie("Logan", "Action", "Wolverine's last run.",
                                        "https://upload.wikimedia.org/wikipedia/en/3/37/Logan_2017_poster.jpg", 125,
                                        4.7, 350, 1650, false));

                        movies.add(new Movie("Avatar: Way of Water", "Sci-Fi", "Return to Pandora.",
                                        "https://upload.wikimedia.org/wikipedia/en/5/54/Avatar_The_Way_of_Water_poster.jpg",
                                        192, 4.8, 450, 2200, true));
                        movies.get(movies.size() - 1).setTrailerUrl("https://www.youtube.com/embed/d9MyW72ELq0");

                        movies.add(new Movie("Super Mario Bros.", "Animation", "Let's-a go!",
                                        "https://upload.wikimedia.org/wikipedia/en/4/44/The_Super_Mario_Bros._Movie_poster.jpg",
                                        92, 4.6, 350, 1800, true));
                        movies.get(movies.size() - 1).setTrailerUrl("https://www.youtube.com/embed/TnGl01FkMMo");

                        movies.add(new Movie("Oppenheimer", "History", "The world forever changes.",
                                        "https://upload.wikimedia.org/wikipedia/en/4/4a/Oppenheimer_%28film%29.jpg",
                                        180, 4.9, 450, 2400, true));
                        movies.get(movies.size() - 1).setTrailerUrl("https://www.youtube.com/embed/uYPbbksJxIg");

                        movies.add(new Movie("Get Out", "Horror", "Just because you're invited...",
                                        "https://upload.wikimedia.org/wikipedia/en/a/a3/Get_Out_poster.png", 104, 4.7,
                                        300, 1500, false));
                        movies.add(new Movie("La La Land", "Romance", "Here's to the fools.",
                                        "https://upload.wikimedia.org/wikipedia/en/a/ab/La_La_Land_%28film%29.png", 125,
                                        4.6, 300, 1550, false));
                        movies.add(new Movie("Dune", "Sci-Fi", "Spice must flow.",
                                        "https://upload.wikimedia.org/wikipedia/en/8/8e/Dune_%282021_film%29.jpg", 125,
                                        4.7, 400, 1800, false));
                        movies.add(new Movie("The Batman", "Action", "Vengeance.",
                                        "https://upload.wikimedia.org/wikipedia/en/f/ff/The_Batman_%28film%29_poster.jpg",
                                        125, 4.6, 400, 1750, false));
                        movies.add(new Movie("Barbie", "Comedy", "She's everything.",
                                        "https://upload.wikimedia.org/wikipedia/en/0/0b/Barbie_2023_poster.jpg", 114,
                                        4.7, 350, 1900, false));
                        movies.add(new Movie("Oppenheimer", "Biopic", "Destroyer of worlds.",
                                        "https://upload.wikimedia.org/wikipedia/en/4/4a/Oppenheimer_%28film%29.jpg",
                                        125, 4.8, 450, 1950, false));
                        movies.add(new Movie("Inside Out", "Animation", "Meet your emotions.",
                                        "https://upload.wikimedia.org/wikipedia/en/0/0a/Inside_Out_%282015_film%29_poster.jpg",
                                        95, 4.8, 300, 1600, false));
                        movies.add(new Movie("Up", "Animation", "Adventure is out there.",
                                        "https://upload.wikimedia.org/wikipedia/en/0/05/Up_%282009_film%29.jpg", 96,
                                        4.8, 300, 1650, false));
                        movies.add(new Movie("Ratatouille", "Animation", "Anyone can cook.",
                                        "https://upload.wikimedia.org/wikipedia/en/5/50/RatatouillePoster.jpg", 111,
                                        4.7, 300, 1550, false));
                        movies.add(new Movie("Finding Nemo", "Animation", "Just keep swimming.",
                                        "https://upload.wikimedia.org/wikipedia/en/2/29/Finding_Nemo.jpg", 100, 4.8,
                                        300, 1650, false));
                        movies.add(new Movie("Monsters, Inc.", "Animation", "We scare because we care.",
                                        "https://upload.wikimedia.org/wikipedia/en/6/63/Monsters_Inc.JPG", 92, 4.7, 300,
                                        1550, false));
                        movies.add(new Movie("Soul", "Animation", "What makes you, you?",
                                        "https://upload.wikimedia.org/wikipedia/en/3/39/Soul_%282020_film%29_poster.jpg",
                                        100, 4.7, 300, 1600, false));
                        movies.add(new Movie("Coco", "Animation", "Remember me.",
                                        "https://upload.wikimedia.org/wikipedia/en/9/98/Coco_%282017_film%29_poster.jpg",
                                        105, 4.8, 300, 1550, false));
                        movies.add(new Movie("Pulp Fiction", "Crime", "Royale with Cheese.",
                                        "https://upload.wikimedia.org/wikipedia/en/3/3b/Pulp_Fiction_%281994%29_poster.jpg",
                                        125, 4.8, 350, 1800, false));
                        movies.add(new Movie("Forrest Gump", "Drama", "Run Forrest Run.",
                                        "https://upload.wikimedia.org/wikipedia/en/6/67/Forrest_Gump_poster.jpg", 125,
                                        4.8, 350, 1850, false));

                        movieRepo.saveAll(movies);

                        List<Showtime> showtimes = new ArrayList<>();
                        LocalDateTime baseTime = LocalDateTime.now().withHour(10).withMinute(0);
                        java.util.Random rand = new java.util.Random();

                        for (Movie m : movies) {
                                if (m.isUpcoming())
                                        continue;
                                // Add showtimes for all theaters
                                for (Theater t : allTheaters) {

                                        int numShowtimes = 2 + rand.nextInt(3); // 2, 3, or 4

                                        for (int s = 0; s < numShowtimes; s++) {
                                                String screenName = "Screen " + (s + 1);

                                                int startHour = 10 + (s * 4);

                                                double priceBase = 200 + (s * 20);

                                                showtimes.add(new Showtime(m, t, screenName,
                                                                baseTime.plusHours(startHour - 10)
                                                                                .plusMinutes((rand.nextInt(4)) * 15),
                                                                priceBase, priceBase + 100, priceBase + 250));
                                        }
                                }
                        }
                        showtimeRepo.saveAll(showtimes);

                        // Create bookings
                        User demoUser = new User("Lakshmi", "lakshmi@example.com", "lakshmi123", "9999999999");
                        demoUser.setWalletBalance(5000.0);
                        userRepo.save(demoUser);

                        String[] randomNames = { "Rahul Sharma", "Priya Patel", "Amit Kumar", "Sneha Gupta",
                                        "Vikram Singh",
                                        "Anjali Rao", "Rohit Verma", "Kavita Reddy", "Suresh Menon", "Neha Joshi",
                                        "Karan Malhotra", "Divya Nair", "Arjun Das", "Meera Iyer", "Varun Kapoor" };

                        java.util.Random random = new java.util.Random();
                        for (Showtime st : showtimes) {

                                if (true) {

                                        int numSeats = random.nextInt(8) + 8;
                                        List<String> selectedSeats = new ArrayList<>();
                                        double total = 0;

                                        String bookerName = randomNames[random.nextInt(randomNames.length)];

                                        for (int j = 0; j < numSeats; j++) {
                                                String type = (new String[] { "P", "G", "S" })[random.nextInt(3)];

                                                int maxRow = type.equals("P") ? 2 : (type.equals("G") ? 3 : 3);
                                                int maxCol = type.equals("P") ? 8 : (type.equals("G") ? 10 : 12);

                                                int row = random.nextInt(maxRow) + 1;
                                                int col = random.nextInt(maxCol) + 1;
                                                String sName = type + row + "-" + col;

                                                if (!selectedSeats.contains(sName)) {
                                                        selectedSeats.add(sName);
                                                        if (type.equals("P"))
                                                                total += st.getPricePremium();
                                                        else if (type.equals("G"))
                                                                total += st.getPriceGold();
                                                        else
                                                                total += st.getPriceSilver();
                                                }
                                        }

                                        if (!selectedSeats.isEmpty()) {
                                                String seatsStr = String.join(", ", selectedSeats);
                                                Booking b = new Booking(demoUser, st, bookerName, seatsStr, total);
                                                b.setStatus("BOOKED");
                                                bookingRepo.save(b);
                                                createFakeSeats(st, seatsStr, b, seatRepo);
                                        }
                                }
                        }

                        bookingRepo.flush();
                        seatRepo.flush();

                        // Add cancelled booking
                        if (!showtimes.isEmpty()) {
                                Showtime st3 = showtimes.get(0);
                                Booking b3 = new Booking(admin, st3, "Admin User", "S2-10, S2-11", 600.0);
                                b3.setStatus("CANCELLED");
                                bookingRepo.save(b3);
                        }

                        User alice = new User("Alice Green", "alice@example.com", "password", "9876543211");
                        User bob = new User("Bob Brown", "bob@example.com", "password", "9876543212");
                        User charlie = new User("Charlie Black", "charlie@example.com", "password", "9876543213");
                        userRepo.saveAll(List.of(alice, bob, charlie));

                        // Create Reviews
                        if (!movies.isEmpty()) {
                                Movie inception = movies.get(0);
                                reviewRepo.save(new com.example.cineverse.models.Review(admin, inception, 5,
                                                "An absolute masterpiece of cinema!"));
                                reviewRepo.save(new com.example.cineverse.models.Review(demoUser, inception, 4,
                                                "A bit confusing, but the visuals are stunning."));
                                reviewRepo.save(new com.example.cineverse.models.Review(alice, inception, 5,
                                                "Mind blowing. Nolan does it again."));

                                Movie titanic = movies.get(3);
                                reviewRepo.save(new com.example.cineverse.models.Review(admin, titanic, 5,
                                                "I cry every single time. 10/10."));
                                reviewRepo.save(new com.example.cineverse.models.Review(demoUser, titanic, 3,
                                                "Too long for my taste, but classic."));
                                reviewRepo.save(new com.example.cineverse.models.Review(bob, titanic, 4,
                                                "Beautiful love story."));

                                Movie rrr = movies.get(13);
                                reviewRepo.save(new com.example.cineverse.models.Review(admin, rrr, 5,
                                                "Pure adrenaline! The action sequences are insane."));
                                reviewRepo.save(new com.example.cineverse.models.Review(charlie, rrr, 5,
                                                "Best theatrical experience ever!"));

                                Movie avatar = movies.get(4);
                                reviewRepo.save(new com.example.cineverse.models.Review(admin, avatar, 4,
                                                "Visually spectacular, story is okay."));
                                reviewRepo.save(new com.example.cineverse.models.Review(alice, avatar, 5,
                                                "Must watch in 3D!"));

                                Movie joker = movies.get(6);
                                reviewRepo.save(new com.example.cineverse.models.Review(admin, joker, 5,
                                                "Joaquin Phoenix is a legend."));
                                reviewRepo.save(new com.example.cineverse.models.Review(bob, joker, 4,
                                                "Dark and disturbing but great."));
                        }

                        couponRepo.saveAll(new ArrayList<>(
                                        List.of(new Coupon("MAX125", 25, java.time.LocalDate.now().plusMonths(3)))));
                };
        }

        private void createFakeSeats(Showtime showtime, String seatStr, Booking booking, SeatRepository seatRepo) {
                String[] seats = seatStr.split(", ");
                for (String s : seats) {
                        String type = "";
                        double price = 0;
                        if (s.startsWith("P")) {
                                type = "PREMIUM";
                                price = showtime.getPricePremium();
                        } else if (s.startsWith("G")) {
                                type = "GOLD";
                                price = showtime.getPriceGold();
                        } else {
                                type = "SILVER";
                                price = showtime.getPriceSilver();
                        }
                        com.example.cineverse.models.Seat seat = new com.example.cineverse.models.Seat(s, type,
                                        showtime, price);
                        seat.setBooked(true);
                        seat.setBooking(booking);
                        seatRepo.save(seat);
                }
        }
}
