package com.codeup.foodtruckfinder.controllers;

import com.codeup.foodtruckfinder.models.Review;
import com.codeup.foodtruckfinder.models.Truck;
import com.codeup.foodtruckfinder.repositories.ReviewRepository;
import com.codeup.foodtruckfinder.repositories.TruckRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TruckController {
    private final TruckRepository truckDao;
    private final ReviewRepository reviewDao;

    public TruckController(TruckRepository truckDao, ReviewRepository reviewDao) {
        this.truckDao = truckDao;
        this.reviewDao = reviewDao;
    }

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("rating", reviewDao.findAll());
        model.addAttribute("trucks", truckDao.findAll());
        return "index";
    }

    @GetMapping("/truck/{id}/edit")
    public String editTruck(@PathVariable Long id, Model model){
        model.addAttribute("truck", truckDao.getById(id));
        return "truck/editTruck";
    }

    @PostMapping("/truck/editTruck")
    public String postEditTruck(@ModelAttribute Truck truck){
        truckDao.save(truck);
        return "redirect:/truck/" + truck.getId() + "/show";
    }

    @GetMapping("/truck/{id}/show")
    public String showTruck(@PathVariable Long id, Model model){
        model.addAttribute("truck", truckDao.getTruckById(id));
        return "truck/individual";
    }

    @GetMapping("/truck/{id}/profile")
    public String truckProfile(@PathVariable Long id, Model model){
        model.addAttribute("truck", truckDao.getById(id));
        return "truck/individual";
    }







}
