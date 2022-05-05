package com.codeup.foodtruckfinder.repositories;

import com.codeup.foodtruckfinder.models.Review;
import com.codeup.foodtruckfinder.models.Truck;
import com.codeup.foodtruckfinder.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> getReviewsByTruck(Truck truck);
    List<Review> getReviewsByUserOrderByIdDesc(User user);
}
