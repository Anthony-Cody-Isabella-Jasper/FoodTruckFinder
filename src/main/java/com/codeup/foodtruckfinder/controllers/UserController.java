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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
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
        return "sign-up";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model, RedirectAttributes redirAttrs) {
        if (userDao.existsUserByEmail(user.getEmail())) {
            model.addAttribute("user", new User());
            model.addAttribute("message", "Email already exists. Please click on \"Forgot Password\" when logging in to retrieve your password.");
            return "sign-up";
        } else if (userDao.existsUserByUsername(user.getUsername())) {
            model.addAttribute("user", new User());
            model.addAttribute("message", "Username already exists. Please pick a different username.");
            return "sign-up";
        }
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        if (user.isTruckOwner()) {
            PendingTruck pendingTruck = new PendingTruck(user.getUsername(), user.getPassword(), user.getEmail());
            pendingTruckDao.save(pendingTruck);
            List<User> admins = userDao.findAllAdmins();
            for (User admin : admins) {
                emailService.prepareAndSend(admin, "New Food Truck", "A new food truck is awaiting approval on StreatFoods. Please log in for review.");
            }
            redirAttrs.addFlashAttribute("fromSignup", true);
            return "redirect:/pending";
        } else {
            userDao.save(user);
        }
        return "redirect:/login";
    }

    @GetMapping("/pending")
    public String pendingApproval(Model model) {
        if (model.getAttribute("fromSignup") != null) {
            return "pendingTruck";
        }
        return "redirect:/";
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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.isTruckOwner() || user.getId() != id) {
            return "redirect:/";
        }
        model.addAttribute("user", userDao.getById(id));
        model.addAttribute("favorites", userDao.getById(id).getFavoriteTrucks());
        return "userProfile";
    }


    @GetMapping("/about")
    public String aboutUs(Model model) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "about";
    }

    @GetMapping("/editUser/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.isTruckOwner() || user.getId() != id) {
            return "redirect:/";
        }
        model.addAttribute("user", userDao.getById(id));
        return "editUser";
    }

    @PostMapping("/editUser/{id}")
    public String editUser(Model model, @ModelAttribute User user, HttpSession session) {
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User newUser = userDao.getById(loggedUser.getId());
        String dbUser = user.getEmail();
        String dbUsername = user.getUsername();
        if (userDao.existsUserByEmail(user.getEmail()) && !dbUser.equals(loggedUser.getEmail())) {
            model.addAttribute("user", userDao.getById(user.getId()));
            model.addAttribute("message", "Email already exists. Please click on \"Forgot Password\" when logging in to retrieve your password.");
            return "/editUser";
        } else if (userDao.existsUserByUsername(user.getUsername()) && !dbUsername.equals(loggedUser.getUsername())) {
            model.addAttribute("user", userDao.getById(user.getId()));
            model.addAttribute("message", "Username already exists. Please pick a different username.");
            return "/editUser";
        }
//        newUser.setPassword(loggedUser.getPassword());
//        newUser.setTruckOwner(loggedUser.isTruckOwner());
//        newUser.setAdmin(loggedUser.isAdmin());
//        newUser.setFavoriteTrucks(loggedUser.getFavoriteTrucks());
        user.setPassword(loggedUser.getPassword());
        user.setTruckOwner(loggedUser.isTruckOwner());
        user.setAdmin(loggedUser.isAdmin());
        user.setFavoriteTrucks(loggedUser.getFavoriteTrucks());
//        userDao.save(newUser);
        userDao.save(user);
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/admin")
    public String adminView(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            return "redirect:/";
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.isAdmin()) {
            return "redirect:/";
        }
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

    @PostMapping("/adminEditUser")
    public String adminEditUser(@RequestParam(name = "userId") Long userId, @RequestParam(name = "userUsername") String username, @RequestParam(name = "userEmail") String email, @RequestParam(name = "userTruckOwner", required = false) String truckOwner, @RequestParam(name = "userAdmin", required = false) String admin) {
        User user = userDao.getById(userId);
        user.setUsername(username);
        user.setEmail(email);
        user.setTruckOwner(truckOwner != null);
        user.setAdmin(admin != null);
        userDao.save(user);
        return "redirect:/admin";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam Long userId, HttpSession httpSession) {
        User userToDelete = userDao.getById(userId);
        userDao.deleteUserConfirmation(userId);
        userDao.deleteUserFavorite(userId);
        if (userToDelete.isTruckOwner()) {
            long userTruckId = userToDelete.getTruck().getId();
            userDao.deleteTruckConfirmation(userTruckId);
            userDao.deleteTruckFavorite(userTruckId);
            userDao.deleteTruckCuisines(userTruckId);
            truckDao.deleteById(userTruckId);
        } else {
            userDao.deleteById(userId);
        }
        if (userId == ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()) {
            httpSession.invalidate();
            return "redirect:/";
        }
        return "redirect:/admin";
    }

    @PostMapping("/adminEditTruck")
    public String adminEditTruck(@RequestParam(name = "truckId") Long truckId, @RequestParam(name = "truckName") String name, @RequestParam(name = "truckDescription") String description) {
        Truck truck = truckDao.getById(truckId);
        truck.setName(name);
        truck.setDescription(description);
        truckDao.save(truck);
        return "redirect:/admin";
    }

    @PostMapping("/deleteTruck")
    public String deleteTruck(@RequestParam Long truckId) {
        userDao.deleteTruckConfirmation(truckId);
        userDao.deleteTruckFavorite(truckId);
        userDao.deleteTruckCuisines(truckId);
        truckDao.deleteById(truckId);
        return "redirect:/admin";
    }

    @PostMapping("/adminEditReview")
    public String adminEditReview(@RequestParam(name = "reviewId") Long reviewId, @RequestParam(name = "reviewRating") int rating, @RequestParam(name = "reviewText") String text) {
        Review review = reviewDao.getById(reviewId);
        review.setRating(rating);
        review.setReviewText(text);
        reviewDao.save(review);
        return "redirect:/admin";
    }

    @PostMapping("/deleteReview")
    public String deleteReview(@RequestParam Long reviewId) {
        reviewDao.deleteById(reviewId);
        return "redirect:/admin";
    }

    @GetMapping("/forgotPassword")
    public String forgotPasswordView(Model model, User user) {
        model.addAttribute("user", user);
        return "forgot-password";
    }

    @PostMapping("/forgotPassword")
    public String forgotPasswordSubmission(@ModelAttribute User user) {
        emailService.prepareAndSend(user, "Reset Password", "http://streatfoods.world/resetPassword?fromEmail=" + user.getEmail());
        return "redirect:/login";
    }

    @GetMapping("/resetPassword")
    public String resetPasswordForm(Model model, User user, @RequestParam(name = "fromEmail", required = false) String fromEmail, RedirectAttributes redirAttrs) {
        if (userDao.existsUserByEmail(fromEmail)) {
            user.setEmail(fromEmail);
            model.addAttribute("user", user);
            return "reset-password";
        }
        redirAttrs.addFlashAttribute("message", "You have not signed up with that email yet. Please sign up now!");
        return "redirect:/register";
    }

    @PostMapping("/resetPassword")
    public String resetPasswordSubmission(@ModelAttribute User user, @RequestParam(name = "password") String password, RedirectAttributes redirAttrs, HttpSession session) {
        User userTest = userDao.findByEmail(user.getEmail());
        if (userTest.isAdmin()) {
            redirAttrs.addFlashAttribute("message", "You cannot reset an admin password. Please login as admin or contact the website administrator.");
        } else {
            userTest.setPassword(passwordEncoder.encode(password));
            userDao.save(userTest);
            session.invalidate();
        }
        return "redirect:/login";
    }

    @GetMapping("/approve")
    public String approveUser(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.isAdmin()) {
            return "redirect:/";
        }
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
        newTruck.setDescription("");
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