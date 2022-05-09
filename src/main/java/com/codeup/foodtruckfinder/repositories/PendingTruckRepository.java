package com.codeup.foodtruckfinder.repositories;

import com.codeup.foodtruckfinder.models.PendingTruck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingTruckRepository extends JpaRepository<PendingTruck, Long> {
}
