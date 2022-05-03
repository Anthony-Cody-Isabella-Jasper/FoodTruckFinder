package com.codeup.foodtruckfinder.controllers;

import com.codeup.foodtruckfinder.models.User;
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
public class UserController {
    private final UserRepository userDao;
    private final ReviewRepository reviewDao;
    private final TruckRepository truckDao;

    public UserController(UserRepository userDao, ReviewRepository reviewDao, TruckRepository truckDao) {
        this.userDao = userDao;
        this.reviewDao = reviewDao;
        this.truckDao = truckDao;
    }

    @GetMapping("/register")
    public String registerUserForm(Model model) {
        model.addAttribute("user", new User());
        return "/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userDao.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public String logUser(){
        return "redirect:/profile";
    }

    @GetMapping("/{id}/profile")
    public String profile(@PathVariable long id, Model model){
        model.addAttribute("user", userDao.getById(id));
        model.addAttribute("favorites", userDao.getById(id).getFavoriteTrucks());
        model.addAttribute("reviews", reviewDao.getReviewsByUserOrderByIdDesc(userDao.getById(id)));
        return "profile";
    }

    @GetMapping("/about")
    public String aboutUs() {
        return "about";
    }

    @GetMapping("/editUser/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userDao.getById(id));
        return "editUser";
    }

    @PostMapping("/editUser/{id}")
    public String editUser(@ModelAttribute User user) {
        userDao.save(user);
        return "redirect:/profile";
    }
}
