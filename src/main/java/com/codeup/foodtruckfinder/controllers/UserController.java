package com.codeup.foodtruckfinder.controllers;

import com.codeup.foodtruckfinder.models.Truck;
import com.codeup.foodtruckfinder.models.PendingTruck;
import com.codeup.foodtruckfinder.models.User;
import com.codeup.foodtruckfinder.repositories.PendingTruckRepository;
import com.codeup.foodtruckfinder.repositories.ReviewRepository;
import com.codeup.foodtruckfinder.repositories.TruckRepository;
import com.codeup.foodtruckfinder.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private final UserRepository userDao;
    private final TruckRepository truckDao;
    private PasswordEncoder passwordEncoder;
    private final PendingTruckRepository pendingTruckDao;

    public UserController(UserRepository userDao, ReviewRepository reviewDao, TruckRepository truckDao, PasswordEncoder passwordEncoder, PendingTruckRepository pendingTruckDao) {
        this.userDao = userDao;
        this.truckDao = truckDao;
        this.passwordEncoder = passwordEncoder;
        this.pendingTruckDao = pendingTruckDao;
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
        if (user.isTruckOwner()) {
            PendingTruck pendingTruck = new PendingTruck(user.getUsername(), user.getPassword(), user.getEmail());
            pendingTruckDao.save(pendingTruck);
            return "redirect:/pending";
        } else {
            userDao.save(user);
        } return "redirect:/login";
    }

    @GetMapping("/pending")
    public String pendingApproval() {
        return "/pending";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String logUser() {
        return "redirect:/index";
    }


    @GetMapping("/{id}/profile")
    public String profile(@PathVariable Long id, Model model) {
        model.addAttribute("user", userDao.getById(id));
        model.addAttribute("favorites", userDao.getById(id).getFavoriteTrucks());
        return "/profile";
    }


    @GetMapping("/about")
    public String aboutUs(Model model) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "about";
    }

    @GetMapping("/editUser/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userDao.getById(id));
        return "editUser";
    }

    @PostMapping("/editUser/{id}")
    public String editUser(@ModelAttribute User user, HttpSession session, @RequestParam("oldPass") String oldPass, @RequestParam("newPass") String newPass) {
        if(passwordEncoder.matches(oldPass, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPass));
        }
        userDao.save(user);
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/admin")
    public String adminView(Model model){
        model.addAttribute("users", userDao.findAll());
        model.addAttribute("trucks", truckDao.findAll());
        return "admin";
    }


    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam Long userId) {
//        User user = userDao.getById(userId);
        userDao.deleteUserFavorite(userId);
        userDao.deleteById(userId);
        return "redirect:/index";
    }

    @PostMapping("/deleteTruck")
    public String deleteTruck(@RequestParam Long truckId){
        Truck truck = truckDao.getById(truckId);
        truckDao.delete(truck);
        return "redirect:/index";
    }

    @GetMapping("/forgotPassword")
    public String forgotPasswordView(Model model) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "/forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String forgotPasswordSubmission(Model model, User user) {
        String userEmail = user.getEmail();

        if(userEmail != null) {
        }


        return "redirect:/login";
    }

}
