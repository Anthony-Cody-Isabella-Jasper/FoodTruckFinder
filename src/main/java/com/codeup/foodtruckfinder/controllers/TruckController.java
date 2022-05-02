package com.codeup.foodtruckfinder.controllers;

import com.codeup.foodtruckfinder.repositories.TruckRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TruckController {
    private final TruckRepository truckDao;

    public TruckController(TruckRepository truckDao) {
        this.truckDao = truckDao;
    }

    @GetMapping
    public String index(){
        return "index";
    }

    @GetMapping
    public String editTruck(){
        return "editTruck";
    }

    @GetMapping
    public String showTruck(){
        return "individual";
    }

    @GetMapping
    public String truckProfile(){
        return "profile";
    }







}
