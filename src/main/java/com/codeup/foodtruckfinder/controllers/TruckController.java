package com.codeup.foodtruckfinder.controllers;

import com.codeup.foodtruckfinder.models.Truck;
import com.codeup.foodtruckfinder.repositories.CuisineRepository;
import com.codeup.foodtruckfinder.repositories.ReviewRepository;
import com.codeup.foodtruckfinder.repositories.TruckRepository;
import com.codeup.foodtruckfinder.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TruckController {
    private final TruckRepository truckDao;
    private final CuisineRepository cuisineDao;
    private final UserRepository userDao;

    public TruckController(TruckRepository truckDao, ReviewRepository reviewDao, CuisineRepository cuisineDao, UserRepository userDao) {
        this.truckDao = truckDao;
        this.cuisineDao = cuisineDao;
        this.userDao = userDao;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("cuisines", cuisineDao.findAll());
        model.addAttribute("trucks", truckDao.findAll());
        return "index";
    }

    @PostMapping("/")
    public String filteredIndex(Model model, @RequestParam(name = "filterCuisine") String filterCuisine, @RequestParam(name = "vegan", required = false) boolean vegan, @RequestParam(name = "vegetarian", required = false) boolean vegetarian) {
        if (filterCuisine.equals("all")) {
            model.addAttribute("trucks", truckDao.filterTrucks(vegetarian, vegan));
        } else {
            model.addAttribute("trucks", truckDao.filterTrucks(filterCuisine, vegetarian, vegan));
        }
        model.addAttribute("cuisines", cuisineDao.findAll());
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
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("truck", truck);
        return "truck/individual";
    }

    @GetMapping("/truck/{id}/profile")
    public String truckProfile(@PathVariable Long id, Model model) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("truck", truckDao.getById(id));
        return "truck/individual";
    }


}
