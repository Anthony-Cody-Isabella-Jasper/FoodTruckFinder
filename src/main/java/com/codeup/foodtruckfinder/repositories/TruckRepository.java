package com.codeup.foodtruckfinder.repositories;

import com.codeup.foodtruckfinder.models.TruckPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import com.codeup.foodtruckfinder.models.Truck;

import java.util.List;

public interface TruckRepository extends JpaRepository <Truck, Long> {
    Truck getTruckById(long id);
    Truck getByTruckPictures();
}
