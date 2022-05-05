package com.codeup.foodtruckfinder.controllers;

import com.codeup.foodtruckfinder.models.Cuisine;
import com.codeup.foodtruckfinder.models.Truck;
import com.codeup.foodtruckfinder.repositories.CuisineRepository;
import com.codeup.foodtruckfinder.repositories.ReviewRepository;
import com.codeup.foodtruckfinder.repositories.TruckRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TruckController {
    private final TruckRepository truckDao;
    private final ReviewRepository reviewDao;
    private final CuisineRepository cuisineDao;

    public TruckController(TruckRepository truckDao, ReviewRepository reviewDao, CuisineRepository cuisineDao) {
        this.truckDao = truckDao;
        this.reviewDao = reviewDao;
        this.cuisineDao = cuisineDao;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("cuisines", cuisineDao.findAll());
        model.addAttribute("rating", reviewDao.findAll());
        model.addAttribute("trucks", truckDao.findAll());
        return "index";
    }

    @PostMapping("/")
    public String filteredIndex(Model model, @RequestParam(name = "filterCuisine") String filterCuisine, @RequestParam(name = "vegan") boolean vegan, @RequestParam(name = "vegetarian") boolean vegetarian) {
        if(filterCuisine.equals("all")) {
            model.addAttribute("trucks", truckDao.filterTrucks(vegetarian, vegan));
        } else {
            model.addAttribute("trucks", truckDao.filterTrucks(filterCuisine, vegetarian, vegan));
        }
        model.addAttribute("cuisines", cuisineDao.findAll());
        model.addAttribute("rating", reviewDao.findAll());
        return "index";
    }

    @GetMapping("/truck/{id}/edit")
    public String editTruck(@PathVariable Long id, Model model) {
        model.addAttribute("truck", truckDao.getById(id));
        return "truck/editTruck";
    }

    @PostMapping("/truck/editTruck")
    public String postEditTruck(@ModelAttribute Truck truck) {
        truckDao.save(truck);
        return "redirect:/truck/" + truck.getId() + "/show";
    }

    @GetMapping("/truck/{id}/show")
    public String showTruck(@PathVariable Long id, Model model) {
        Truck truck = truckDao.getTruckById(id);
        model.addAttribute("truck", truck);
        model.addAttribute("reviews", reviewDao.getReviewsByTruck(truck));
        return "truck/individual";
    }

    @GetMapping("/truck/{id}/profile")
    public String truckProfile(@PathVariable Long id, Model model) {
        model.addAttribute("truck", truckDao.getById(id));
        return "truck/individual";
    }


}
