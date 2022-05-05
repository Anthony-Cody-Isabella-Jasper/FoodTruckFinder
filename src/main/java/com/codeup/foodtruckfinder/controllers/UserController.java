package com.codeup.foodtruckfinder.controllers;

import com.codeup.foodtruckfinder.models.User;
import com.codeup.foodtruckfinder.repositories.ReviewRepository;
import com.codeup.foodtruckfinder.repositories.TruckRepository;
import com.codeup.foodtruckfinder.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    private final UserRepository userDao;
    private final ReviewRepository reviewDao;
    private final TruckRepository truckDao;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userDao, ReviewRepository reviewDao, TruckRepository truckDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.reviewDao = reviewDao;
        this.truckDao = truckDao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerUserForm(Model model) {
        model.addAttribute("user", new User());
        return "/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        userDao.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public String logUser(){
        return "redirect:/index";
    }

<<<<<<< HEAD
    @GetMapping("/{username}/profile")
    public String profile(@PathVariable String username, Model model){
        model.addAttribute("user", userDao.findByUsername(username));
        model.addAttribute("favorites", userDao.findByUsername(username).getFavoriteTrucks());
        model.addAttribute("reviews", reviewDao.getReviewsByUserOrderByIdDesc(userDao.findByUsername(username)));
=======
    @GetMapping("/{id}/profile")
    public String profile(@PathVariable Long id, Model model){
        model.addAttribute("user", userDao.getById(id));
        model.addAttribute("favorites", userDao.getById(id).getFavoriteTrucks());
        model.addAttribute("reviews", reviewDao.getReviewsByUserOrderByIdDesc(userDao.getById(id)));
>>>>>>> origin/main
        return "profile";
    }

    @GetMapping("/about")
    public String aboutUs() {
        return "about";
    }

    @GetMapping("/editUser/{username}")
    public String editUserForm(@PathVariable String username, Model model) {
        User editUser = userDao.findByUsername(username);
        model.addAttribute("user", editUser);
        return "editUser";
    }

    @PostMapping("/editUser/{username}")
    public String editUser(@ModelAttribute User user) {
        userDao.save(user);
        return "redirect:/" + user.getUsername() + "/profile";
    }

}
