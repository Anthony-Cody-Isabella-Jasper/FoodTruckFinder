package com.codeup.foodtruckfinder.controllers;

import com.codeup.foodtruckfinder.models.Review;
import com.codeup.foodtruckfinder.models.Truck;
import com.codeup.foodtruckfinder.models.PendingTruck;
import com.codeup.foodtruckfinder.models.User;
import com.codeup.foodtruckfinder.repositories.PendingTruckRepository;
import com.codeup.foodtruckfinder.repositories.ReviewRepository;
import com.codeup.foodtruckfinder.repositories.TruckRepository;
import com.codeup.foodtruckfinder.repositories.UserRepository;
import com.codeup.foodtruckfinder.services.EmailService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    private final UserRepository userDao;
    private final TruckRepository truckDao;
    private final PasswordEncoder passwordEncoder;
    private final PendingTruckRepository pendingTruckDao;
    private final ReviewRepository reviewDao;
    private final EmailService emailService;


    public UserController(UserRepository userDao, ReviewRepository reviewDao, TruckRepository truckDao, PasswordEncoder passwordEncoder, PendingTruckRepository pendingTruckDao, EmailService emailService) {
        this.userDao = userDao;
        this.truckDao = truckDao;
        this.passwordEncoder = passwordEncoder;
        this.pendingTruckDao = pendingTruckDao;
        this.reviewDao = reviewDao;
        this.emailService = emailService;
    }

    @GetMapping("/register")
    public String registerUserForm(Model model) {
        model.addAttribute("user", new User());
        return "/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userDao.existsUserByEmail(user.getEmail())) {
            model.addAttribute("user", new User());
            model.addAttribute("message", "Email already exists. Please click on \"Forgot Password\" when logging in to retrieve your password.");
            return "/register";
        } else if (userDao.existsUserByUsername(user.getUsername())) {
            model.addAttribute("user", new User());
            model.addAttribute("message", "Username already exists. Please pick a different username.");
            return "/register";
        }
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        if (user.isTruckOwner()) {
            PendingTruck pendingTruck = new PendingTruck(user.getUsername(), user.getPassword(), user.getEmail());
            pendingTruckDao.save(pendingTruck);
            return "redirect:/pending";
        } else {
            userDao.save(user);
        }
        return "redirect:/login";
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
    public String editUser(Model model, @ModelAttribute User user, HttpSession session, @RequestParam("oldPass") String oldPass, @RequestParam("newPass") String newPass) {
        if (userDao.existsUserByEmail(user.getEmail())) {
            model.addAttribute("user", userDao.getById(user.getId()));
            model.addAttribute("message", "Email already exists. Please click on \"Forgot Password\" when logging in to retrieve your password.");
            return "/editUser";
        } else if (userDao.existsUserByUsername(user.getUsername())) {
            model.addAttribute("user", userDao.getById(user.getId()));
            model.addAttribute("message", "Username already exists. Please pick a different username.");
            return "/editUser";
        }
        if (passwordEncoder.matches(oldPass, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPass));
        }
        userDao.save(user);
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/admin")
    public String adminView(Model model) {
        model.addAttribute("users", userDao.findAll());
        model.addAttribute("trucks", truckDao.findAll());
        model.addAttribute("reviews", reviewDao.findAll());
        return "admin";
    }

    @PostMapping("/deleteUserFromUser")
    public String deleteUserFromUser(@RequestParam Long userId, HttpSession session) {
        userDao.deleteUserConfirmation(userId);
        userDao.deleteUserFavorite(userId);
        userDao.deleteById(userId);
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/admin")
    public String adminSearch(Model model, @RequestParam(name = "usernameSearch", required = false) String usernameSearch, @RequestParam(name = "truckSearch", required = false) String truckSearch, @RequestParam(name = "reviewSearch", required = false) String reviewSearch, @RequestParam(name = "searchType") String searchType) {
        model.addAttribute("users", userDao.findAll());
        model.addAttribute("trucks", truckDao.findAll());
        model.addAttribute("reviews", reviewDao.findAll());
        switch (searchType) {
            case "user":
                model.addAttribute("users", userDao.adminUserSearch(usernameSearch));
                break;
            case "truck":
                model.addAttribute("trucks", truckDao.adminTruckSearch(truckSearch));
                break;
            case "review":
                model.addAttribute("reviews", reviewDao.adminReviewSearch(reviewSearch));
                break;
        }
        return "admin";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam Long userId) {
        userDao.deleteUserConfirmation(userId);
        userDao.deleteUserFavorite(userId);
        userDao.deleteById(userId);
        return "redirect:/admin";
    }

    @PostMapping("/deleteTruck")
    public String deleteTruck(@RequestParam Long truckId) {
        userDao.deleteTruckConfirmation(truckId);
        userDao.deleteTruckFavorite(truckId);
        truckDao.deleteById(truckId);
        return "redirect:/admin";
    }

    @GetMapping("/forgotPassword")
    public String forgotPasswordView(Model model, User user) {
        model.addAttribute("user", user);
        return "/forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String forgotPasswordSubmission(@ModelAttribute User user) {
        emailService.prepareAndSend(user, "Reset Password", "http://localhost:8080/resetPassword");
        return "redirect:/login";
    }

    @GetMapping("/resetPassword")
    public String resetPasswordForm(Model model, User user) {
        model.addAttribute("user", user);
        return "/resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPasswordSubmission(@ModelAttribute User user, @RequestParam(name = "password") String password) {
        User userTest = userDao.findByEmail(user.getEmail());
        userTest.setPassword(passwordEncoder.encode(password));

        userDao.save(userTest);

        return "redirect:/login";
    }


    @PostMapping("/deleteReview")
    public String deleteReview(@RequestParam Long reviewId) {
        reviewDao.deleteById(reviewId);
        return "redirect:/admin";
    }

    @GetMapping("/approve")
    public String approveUser(Model model) {
        model.addAttribute("pendingUsers", pendingTruckDao.findAll());
        return "approve";
    }

    @PostMapping("/approve")
    public String approved(Model model, @RequestParam(name = "pendingId") Long pendingId, @RequestParam(name = "username") String username, @RequestParam(name = "email") String email, @RequestParam(name = "password") String password) {
        if (userDao.existsUserByEmail(email)) {
            model.addAttribute("pendingUsers", pendingTruckDao.findAll());
            model.addAttribute("message", "Email already exists. Please click on \"Forgot Password\" when logging in to retrieve your password.");
            return "approve";
        } else if (userDao.existsUserByUsername(username)) {
            model.addAttribute("pendingUsers", pendingTruckDao.findAll());
            model.addAttribute("message", "Username already exists. Please pick a different username.");
            return "approve";
        }
        User newUser = new User(username, password, email, true, "");
        Truck newTruck = new Truck();
        newTruck.setName("My Truck");
        newUser.setTruck(newTruck);
        newTruck.setTruck_owner(newUser);
        emailService.prepareAndSend(newUser, "StreatFoods Account Approved", "Congratulations! \nYour account has passed the rigorous StreatFoods approval process! Log in now to finish setting up your food truck. \n\nThank you from our team at StreatFoods");
        userDao.save(newUser);
        truckDao.save(newTruck);
        pendingTruckDao.deleteById(pendingId);
        return "redirect:/approve";
    }

    @PostMapping("/reject")
    public String rejected(@RequestParam(name = "pendingId") Long pendingId) {
        emailService.prepareAndSendTruck(pendingTruckDao.getById(pendingId), "StreatFoods Account Rejected", "Unfortunately, \nYour new StreatFoods account has been rejected due to not meeting our website usage guidelines. \n\nHave a nice day from our team at StreatFoods!");
        pendingTruckDao.deleteById(pendingId);
        return "redirect:/approve";
    }
}
