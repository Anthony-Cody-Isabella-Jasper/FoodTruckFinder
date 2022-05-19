package com.codeup.foodtruckfinder.controllers;

import com.codeup.foodtruckfinder.models.Review;
import com.codeup.foodtruckfinder.models.Truck;
import com.codeup.foodtruckfinder.models.User;
import com.codeup.foodtruckfinder.repositories.ReviewRepository;
import com.codeup.foodtruckfinder.repositories.TruckRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReviewController {
    private final ReviewRepository reviewDao;
    private final TruckRepository truckDao;

    public ReviewController(ReviewRepository reviewDao, TruckRepository truckDao) {
        this.reviewDao = reviewDao;
        this.truckDao = truckDao;
    }

    @GetMapping("/review/{id}")
    public String reviewForm(@PathVariable Long id, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.isTruckOwner()) {
            return "redirect:/";
        }
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("truck", truckDao.getTruckById(id));
        model.addAttribute("reviewObj", new Review());
        return "review";
    }

    @PostMapping("/review")
    public String review(@ModelAttribute Review review, @RequestParam("truckId") Long id) {
        review.setUser((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        review.setTruck(truckDao.getById(id));
        reviewDao.save(review);
        return "redirect:/truck/" + review.getTruck().getId() + "/show";
    }

    @GetMapping("/editReview/{id}")
    public String editReviewPage(@PathVariable Long id, Model model) {
        model.addAttribute("review", reviewDao.findById(id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Review blockReview = reviewDao.getById(id);
        long blockId = blockReview.getUser().getId();
        if (user.isTruckOwner()) {
            return "redirect:/";
        }
        if (user.getId() != blockId) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        model.addAttribute("revId", id);
        Review review = reviewDao.getById(id);
        model.addAttribute("truckId", review.getTruck().getId());
        return "edit-review";
    }

    @PostMapping("/editReviews")
    public String editReview(@ModelAttribute Review review, @RequestParam(name = "revId") Long id, @RequestParam(name = "truckId") Long truckId) {
        review.setId(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        review.setUser(user);
        Truck truck = truckDao.getTruckById(truckId);
        review.setTruck(truck);
        reviewDao.save(review);
        return "redirect:" + user.getId() + "userProfile";
    }

    @PostMapping("/deleteUserReview")
    public String deleteUserReview(@RequestParam(name = "revId") Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        reviewDao.deleteById(id);
        return "redirect:" + user.getId() + "userProfile";
    }
}