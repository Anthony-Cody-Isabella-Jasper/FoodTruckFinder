package com.codeup.foodtruckfinder.controllers;

import com.codeup.foodtruckfinder.models.Review;
import com.codeup.foodtruckfinder.models.Truck;
import com.codeup.foodtruckfinder.repositories.ReviewRepository;
import com.codeup.foodtruckfinder.repositories.TruckRepository;
import com.codeup.foodtruckfinder.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
        Truck truck = truckDao.getTruckById(id);
        model.addAttribute("truck", truck);
        model.addAttribute("reviewObj", new Review());
        return "review";
    }

    @PostMapping("/review")
    public String review(@ModelAttribute Review review) {
        review.setUser(userDao.getById(1L));
        reviewDao.save(review);
        return "redirect:/truck/" + review.getTruck().getId() + "/show";
    }


}
