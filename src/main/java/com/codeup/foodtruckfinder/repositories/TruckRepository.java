package com.codeup.foodtruckfinder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codeup.foodtruckfinder.models.Truck;

public interface TruckRepository extends JpaRepository <Truck, Long> {

}
