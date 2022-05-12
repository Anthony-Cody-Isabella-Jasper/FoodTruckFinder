package com.codeup.foodtruckfinder.repositories;

import com.codeup.foodtruckfinder.models.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CuisineRepository extends JpaRepository<Cuisine, Long> {
    @Query(value = "SELECT * FROM cuisines ORDER BY name ASC", nativeQuery = true)
    List<Cuisine> getCuisinesAlphabetical();
}