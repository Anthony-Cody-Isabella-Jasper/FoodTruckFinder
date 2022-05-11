package com.codeup.foodtruckfinder.controllers;

import com.codeup.foodtruckfinder.models.*;
import com.codeup.foodtruckfinder.repositories.CuisineRepository;
import com.codeup.foodtruckfinder.repositories.MenuRepository;
import com.codeup.foodtruckfinder.repositories.TruckRepository;
import com.codeup.foodtruckfinder.repositories.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TruckController {
    private final TruckRepository truckDao;
    private final CuisineRepository cuisineDao;
    private final UserRepository userDao;
    private final MenuRepository menuDao;

    public TruckController(MenuRepository menuDao, TruckRepository truckDao, CuisineRepository cuisineDao, UserRepository userDao) {
        this.truckDao = truckDao;
        this.cuisineDao = cuisineDao;
        this.userDao = userDao;
        this.menuDao = menuDao;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("cuisines", cuisineDao.findAll());
        model.addAttribute("trucks", truckDao.findAll());
        return "index";
    }

    @PostMapping("/search")
    public String searchIndex(Model model, @RequestParam(name = "search") String search) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("cuisines", cuisineDao.findAll());
        model.addAttribute("trucks", truckDao.searchTrucks(search));
        return "index";
    }

    @PostMapping("/filter")
    public String filteredIndex(Model model, @RequestParam(name = "filterCuisine") String filterCuisine, @RequestParam(name = "vegan", required = false) boolean vegan, @RequestParam(name = "vegetarian", required = false) boolean vegetarian) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (filterCuisine.equals("all")) {
            model.addAttribute("trucks", truckDao.filterTrucks(vegetarian, vegan));
        } else {
            model.addAttribute("trucks", truckDao.filterTrucks(filterCuisine, vegetarian, vegan));
        }
        model.addAttribute("cuisines", cuisineDao.findAll());
        return "index";
    }

    @GetMapping("/truck/{id}/edit")
    public String editTruck(@PathVariable Long id, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("truck", truckDao.getById(id));
        return "truck/editTruck";
    }

    @PostMapping("/truck/editTruck")
    public String postEditTruck(@ModelAttribute Truck truck, @RequestParam String truckPictureUrls) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Truck newTruck = user.getTruck();

        long truckId = user.getTruck().getId();
        truckDao.deletePictures(truckId);

        List<TruckPicture> truckImages = new ArrayList<>();
        String [] urls = truckPictureUrls.split(",");
        for (String url : urls){
            System.out.println(url);
            truckImages.add(new TruckPicture(url, truck));
        }

        newTruck.setTruckPictures(truckImages);
        newTruck.setTruck_owner(user);
        newTruck.setProfile_picture(truck.getProfile_picture());
        newTruck.setName(truck.getName());
        newTruck.setDescription(truck.getDescription());
        newTruck.setPhone(truck.getPhone());


        truckDao.save(newTruck);
        return "redirect:/truck/" + truck.getId() + "/show";
    }

    @GetMapping("/truck/{id}/addMenuItem")
    public String addMenuItem(@PathVariable Long id, Model model){
        List<Cuisine> cuisines = cuisineDao.findAll();
        model.addAttribute("cuisines", cuisines);
        model.addAttribute("menuItem", new Menu());
        model.addAttribute("truck", truckDao.getTruckById(id));
        return "truck/addMenuItems";
    }

    @PostMapping("/truck/addMenuItem")
    public String postAddMenuItem(@ModelAttribute Menu menuItem, @RequestParam(name = "type1") String name){
        List<Menu> menuItems = new ArrayList<>();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Truck truck = truckDao.getById(user.getTruck().getId());
// THIS WILL BE MOVED TO ITS OWN POST MAPPING
        List<Cuisine> newCuisine = new ArrayList<>();
        String[] items = name.split(",");
        for(String item : items) {
            newCuisine.add(cuisineDao.getById(Long.valueOf(item)));
        }

        truck.setCuisines(newCuisine);
//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        menuItem.setTruck(truck);
        menuItems.add(menuItem);
        truck.setMenu(menuItems);
        truckDao.save(truck);
        return "redirect:/truck/" + truck.getId() + "/show";
    }

//    @PostMapping("/truck/pickCuisine")
//    public String pickedCuisine() {
//
//
//        return "redirect:/truck/5/addMenuItem";
//    }

    @GetMapping("/truck/{id}/show")
    public String showTruck(@PathVariable Long id, Model model) {
        Truck truck = truckDao.getTruckById(id);
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("truck", truck);
        return "truck/individual";
    }

    @PostMapping("/truck/{id}/located")
    public String setTruckLocation(@PathVariable Long id, @RequestParam("longitude") Double longitude, @RequestParam("latitude") Double latitude) {
        Truck truck = truckDao.getTruckById(id);
        truck.setLatitude(latitude);
        truck.setLongitude(longitude);
        truckDao.save(truck);
        return "redirect:/truck/" + id + "/show";
    }

    @PostMapping("/truck/{id}/unlocated")
    public String removeTruckLocation(@PathVariable Long id) {
        truckDao.nullLongitude(id);
        truckDao.nullLatitude(id);
        truckDao.unverifyTruck(id);
        return "redirect:/truck/" + id + "/show";
    }
    @PostMapping("/truck/verify")
    public String addVerification(@RequestParam("truckId") Long truckId, @RequestParam("userId") Long userId) {
        User user = userDao.getById(userId);
        List<Truck> confirmedTrucks = user.getConfirmed_trucks();
        confirmedTrucks.add(truckDao.getTruckById(truckId));
        userDao.save(user);
        return "redirect:/truck/" + truckId + "/show";
    }
    @GetMapping("/truck/{id}/profile")
    public String truckProfile(@PathVariable Long id, Model model) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("truck", truckDao.getById(id));
        return "truck/individual";
    }

}
