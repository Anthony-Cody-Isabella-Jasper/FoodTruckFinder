package com.codeup.foodtruckfinder.controllers;

import com.codeup.foodtruckfinder.models.Truck;
import com.codeup.foodtruckfinder.models.User;
import com.codeup.foodtruckfinder.repositories.TruckRepository;
import com.codeup.foodtruckfinder.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FavoriteController {
    private final TruckRepository truckDao;
    private final UserRepository userDao;

    public FavoriteController(TruckRepository truckDao, UserRepository userDao) {
        this.truckDao = truckDao;
        this.userDao = userDao;
    }

    @PostMapping("/addFavorite")
    public String addFavorite(@RequestParam Long truckId, @RequestParam String username) {
        User user = userDao.findByUsername(username);
        List<Truck> fave;
        if (user == null) {
            return "redirect:/";
        } else if(user.getFavoriteTrucks() == null) {
            fave = new ArrayList<>();
            System.out.println("new list created");
        } else {
            fave = user.getFavoriteTrucks();
            System.out.println("not new list");
        }
        fave.add(truckDao.getTruckById(truckId));
        user.setFavoriteTrucks(fave);
        userDao.save(user);
        return "redirect:/" + user.getUsername() + "/profile";
    }

}
