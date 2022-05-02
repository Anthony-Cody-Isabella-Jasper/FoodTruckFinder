package com.codeup.foodtruckfinder.controllers;

import com.codeup.foodtruckfinder.repositories.TruckRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TruckController {
    private final TruckRepository truckDao;

    public TruckController(TruckRepository truckDao) {
        this.truckDao = truckDao;
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/truck/{id}/edit")
    public String editTruck(@PathVariable Long id, Model model){
        model.addAttribute("truck", truckDao.getById(id));
        return "truck/editTruck";
    }

    @PostMapping("/truck/{id}/edit")
    public String postEditTruck()

    @GetMapping("/truck/{id}/show")
    public String showTruck(@PathVariable Long id, Model model){
        model.addAttribute("truck", truckDao.getById(id));
        return "truck/individual";
    }

    @GetMapping("/truck/{id}/profile")
    public String truckProfile(@PathVariable Long id, Model model){
        model.addAttribute("truck", truckDao.getById(id));
        return "truck/profile";
    }







}
