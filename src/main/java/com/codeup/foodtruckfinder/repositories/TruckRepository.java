package com.codeup.foodtruckfinder.repositories;

import com.codeup.foodtruckfinder.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.codeup.foodtruckfinder.models.Truck;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TruckRepository extends JpaRepository<Truck, Long> {
    Truck getTruckById(long id);

    @Query(value = "SELECT DISTINCT t.id, t.description, t.latitude, t.longitude, t.name, t.phone, t.profile_picture, t.truck_owner_id FROM trucks t WHERE t.name LIKE CONCAT('%', ?1, '%')", nativeQuery = true)
    List<Truck> searchTrucks(String search);

    @Query(value = "SELECT DISTINCT t.id, t.description, t.latitude, t.longitude, t.name, t.phone, t.profile_picture, t.truck_owner_id FROM trucks t JOIN trucks_cuisines tc on t.id = tc.truck_id JOIN cuisines c on c.id = tc.cuisine_id JOIN menu_items mi on t.id = mi.truck_id WHERE c.name = ?1 AND mi.vegetarian = ?2 AND mi.vegan = ?3", nativeQuery = true)
    List<Truck> filterTrucks(String cuisine, boolean vegetarian, boolean vegan);

    @Query(value = "SELECT DISTINCT t.id, t.description, t.latitude, t.longitude, t.name, t.phone, t.profile_picture, t.truck_owner_id FROM trucks t JOIN menu_items mi on t.id = mi.truck_id WHERE mi.vegetarian = ?1 AND mi.vegan = ?2", nativeQuery = true)
    List<Truck> filterTrucks(boolean vegetarian, boolean vegan);

    @Query(value = "SELECT DISTINCT t.id, t.description, t.latitude, t.longitude, t.name, t.phone, t.profile_picture, t.truck_owner_id FROM trucks t JOIN trucks_cuisines tc on t.id = tc.truck_id JOIN cuisines c on c.id = tc.cuisine_id WHERE c.name = ?1", nativeQuery = true)
    List<Truck> filterTrucks(String cuisine);

    @Transactional
    @Modifying
    @Query(value = "UPDATE trucks SET longitude = null WHERE id = ?1", nativeQuery = true)
    void nullLongitude(long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE trucks SET latitude = null WHERE id = ?1", nativeQuery = true)
    void nullLatitude(long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_confirmations WHERE truck_id = ?1", nativeQuery = true)
    void unverifyTruck(long id);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM truck_pictures WHERE truck_id = ?1", nativeQuery = true)
    void deletePictures(long id);


    @Query(value = "SELECT * FROM trucks WHERE name LIKE CONCAT('%', ?1, '%')", nativeQuery = true)
    List<Truck> adminTruckSearch(String truckName);

}