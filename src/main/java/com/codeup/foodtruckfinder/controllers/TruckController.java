package com.codeup.foodtruckfinder.controllers;

import com.codeup.foodtruckfinder.repositories.TruckRepository;
import org.springframework.stereotype.Controller;

@Controller
public class TruckController {
    private final TruckRepository truckDao;

    public TruckController(TruckRepository truckDao) {
        this.truckDao = truckDao;
    }
}
