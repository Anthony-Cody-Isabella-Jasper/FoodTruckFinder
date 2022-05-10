package com.codeup.foodtruckfinder.repositories;

import com.codeup.foodtruckfinder.models.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long>{
    Menu getByTruckId(long id);
}
