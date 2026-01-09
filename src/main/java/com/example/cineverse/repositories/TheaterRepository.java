package com.example.cineverse.repositories;

import com.example.cineverse.models.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
    List<Theater> findByCity(String city);

    @Query("SELECT DISTINCT t.city FROM Theater t")
    List<String> findDistinctCities();
}
