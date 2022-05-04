package com.codeup.foodtruckfinder.repositories;

import com.codeup.foodtruckfinder.models.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuisineRepository extends JpaRepository<Cuisine, Long> {
}
