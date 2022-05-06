package com.codeup.foodtruckfinder.controllers;

import com.codeup.foodtruckfinder.models.Truck;
import com.codeup.foodtruckfinder.models.User;
import com.codeup.foodtruckfinder.repositories.TruckRepository;
import com.codeup.foodtruckfinder.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
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
    public Object addFavorite(@RequestParam Long truckId, @RequestParam String username) {
        User user = userDao.findByUsername(username);
        List<Truck> fave;
        if (user == null) {
            return "redirect:/login";
        } else if(user.getFavoriteTrucks() == null) {
            fave = new ArrayList<>();
        } else {
            fave = user.getFavoriteTrucks();
        }

        long userId = user.getId();
        if(user.getFavoriteTrucks().contains(truckDao.getTruckById(truckId)) && userDao.existsById(userId)) {
            return "redirect:/truck/" + truckId + "/show";
        } else {
            fave.add(truckDao.getTruckById(truckId));
            user.setFavoriteTrucks(fave);
            userDao.save(user);
        }
        return "redirect:/" + user.getUsername() + "/profile";
    }

    @PostMapping("/deleteFavorite")
    public String deleteFavorite(@RequestParam Long truckId, @RequestParam String username) {
        User user = userDao.findByUsername(username);
        userDao.deleteFavorite(user.getId(), truckId);
        return "redirect:/" + user.getUsername() + "/profile";
    }

}
