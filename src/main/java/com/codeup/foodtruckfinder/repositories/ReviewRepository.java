package com.codeup.foodtruckfinder.repositories;

import com.codeup.foodtruckfinder.models.Review;
import com.codeup.foodtruckfinder.models.Truck;
import com.codeup.foodtruckfinder.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> getReviewsByTruck(Truck truck);

    List<Review> getReviewsByUserOrderByIdDesc(User user);

    @Transactional
    @Modifying
    @Query(value = "UPDATE reviews SET rating=?1 AND review_text=?2 WHERE id = ?3", nativeQuery = true)
    void update(long id);

    @Query(value = "SELECT * FROM reviews WHERE reviews.review_text LIKE CONCAT('%', ?1, '%')", nativeQuery = true)
    List<Review> adminReviewSearch(String reviewText);
}
