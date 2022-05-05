package com.codeup.foodtruckfinder.controllers;

import com.codeup.foodtruckfinder.models.User;
import com.codeup.foodtruckfinder.repositories.ReviewRepository;
import com.codeup.foodtruckfinder.repositories.TruckRepository;
import com.codeup.foodtruckfinder.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    private final UserRepository userDao;
    private final TruckRepository truckDao;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userDao, ReviewRepository reviewDao, TruckRepository truckDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
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


    @GetMapping("/{username}/profile")
    public String profile(@PathVariable String username, Model model) {
        model.addAttribute("user", userDao.findByUsername(username));
        model.addAttribute("favorites", userDao.findByUsername(username).getFavoriteTrucks());
        return "/profile";
    }



    @GetMapping("/about")
    public String aboutUs(Model model) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
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
