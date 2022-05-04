package com.codeup.foodtruckfinder.repositories;

import com.codeup.foodtruckfinder.models.Cuisine;
import com.codeup.foodtruckfinder.models.TruckPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import com.codeup.foodtruckfinder.models.Truck;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TruckRepository extends JpaRepository <Truck, Long> {
    Truck getTruckById(long id);
    @Query(value = "SELECT * FROM trucks t JOIN trucks_cuisines tc on t.id = tc.truck_id JOIN cuisines c on c.id = tc.cuisine_id WHERE c.name = ?1", nativeQuery = true)
    List<Truck> findTrucksByCuisine(String cuisine);
}
