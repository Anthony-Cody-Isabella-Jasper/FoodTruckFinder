package com.codeup.foodtruckfinder.controllers;

import com.codeup.foodtruckfinder.models.Review;
import com.codeup.foodtruckfinder.models.Truck;
import com.codeup.foodtruckfinder.models.User;
import com.codeup.foodtruckfinder.repositories.ReviewRepository;
import com.codeup.foodtruckfinder.repositories.TruckRepository;
import com.codeup.foodtruckfinder.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReviewController {
    private final ReviewRepository reviewDao;
    private final TruckRepository truckDao;
    private final UserRepository userDao;

    public ReviewController(ReviewRepository reviewDao, TruckRepository truckDao, UserRepository userDao){
        this.reviewDao = reviewDao;
        this.truckDao = truckDao;
        this.userDao = userDao;
    }

    @GetMapping("/review/{id}")
    public String reviewForm(@PathVariable Long id, Model model) {
        model.addAttribute("truck", truckDao.getTruckById(id));
        model.addAttribute("reviewObj", new Review());
        return "review";
    }

    @PostMapping("/review")
    public String review(@ModelAttribute Review review, @RequestParam("truckId") Long id) {
        review.setUser((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        review.setTruck(truckDao.getById(id));
        reviewDao.save(review);
        return "redirect:/truck/" + review.getTruck().getId() + "/show";
    }
}
