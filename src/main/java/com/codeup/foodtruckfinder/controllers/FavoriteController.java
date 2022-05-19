package com.codeup.foodtruckfinder.controllers;

import com.codeup.foodtruckfinder.models.Truck;
import com.codeup.foodtruckfinder.models.User;
import com.codeup.foodtruckfinder.repositories.TruckRepository;
import com.codeup.foodtruckfinder.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String addFavorite(@RequestParam Long truckId, @RequestParam Long id, RedirectAttributes redirAttrs) {
        User user = userDao.getById(id);
        List<Truck> fave;
        if(user.getFavoriteTrucks() == null) {
            fave = new ArrayList<>();
        } else {
            fave = user.getFavoriteTrucks();
        }
        if(user.getFavoriteTrucks().contains(truckDao.getTruckById(truckId)) && userDao.existsById(user.getId())) {
            redirAttrs.addFlashAttribute("message", "This truck is already added to your favorites");
            return "redirect:/truck/" + truckId + "/show";
        } else {
            fave.add(truckDao.getTruckById(truckId));
            user.setFavoriteTrucks(fave);
            userDao.save(user);
        }
        return "redirect:/" + user.getId() + "userProfile";
    }

    @PostMapping("/deleteFavorite")
    public String deleteFavorite(@RequestParam Long truckId, @RequestParam Long id) {
        User user = userDao.getById(id);
        userDao.deleteFavorite(id, truckId);
        return "redirect:/" + user.getId() + "userProfile";
    }
}
