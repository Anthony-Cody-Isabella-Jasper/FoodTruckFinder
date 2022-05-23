package com.codeup.foodtruckfinder.controllers;

import com.codeup.foodtruckfinder.models.*;
import com.codeup.foodtruckfinder.repositories.CuisineRepository;
import com.codeup.foodtruckfinder.repositories.MenuRepository;
import com.codeup.foodtruckfinder.repositories.TruckRepository;
import com.codeup.foodtruckfinder.repositories.UserRepository;
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
        model.addAttribute("cuisines", cuisineDao.getCuisinesAlphabetical());
        model.addAttribute("trucks", truckDao.findAll());
        return "index";
    }

    @PostMapping("/search")
    public String searchIndex(Model model, @RequestParam(name = "search") String search) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("cuisines", cuisineDao.getCuisinesAlphabetical());
        model.addAttribute("trucks", truckDao.searchTrucks(search));
        return "index";
    }

    @PostMapping("/filter")
    public String filteredIndex(Model model, @RequestParam(name = "filterCuisine") String filterCuisine, @RequestParam(name = "vegan", required = false) boolean vegan, @RequestParam(name = "vegetarian", required = false) boolean vegetarian) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if(filterCuisine.equals("all") && (vegan || vegetarian)) {
            model.addAttribute("trucks", truckDao.filterTrucks(vegetarian, vegan));
        } else if (filterCuisine.equals("all")) {
            model.addAttribute("trucks", truckDao.findAll());
        } else if (!vegan && !vegetarian) {
            model.addAttribute("trucks", truckDao.filterTrucks(filterCuisine));
        } else {
            model.addAttribute("trucks", truckDao.filterTrucks(filterCuisine, vegetarian, vegan));
        }
        model.addAttribute("cuisines", cuisineDao.getCuisinesAlphabetical());
        return "index";
    }

    @GetMapping("/truck/{id}/edit")
    public String editTruck(@PathVariable Long id, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.isTruckOwner() || user.getTruck().getId() != id) {
            return "redirect:/";
        }
        List<Cuisine> cuisines = cuisineDao.getCuisinesAlphabetical();
        model.addAttribute("cuisines", cuisines);
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
        String[] urls = truckPictureUrls.split(",");
        for (String url : urls) {
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

    @PostMapping("/truck/deleteTruckAccount")
    public String deleteTruckAccount(@RequestParam Long truckId, HttpSession session) {
        userDao.deleteTruckConfirmation(truckId);
        userDao.deleteTruckFavorite(truckId);
        userDao.deleteTruckCuisines(truckId);
        truckDao.deleteById(truckId);
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/truck/{id}/editmenu")
    public String addMenuItem(@PathVariable Long id, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.isTruckOwner() || user.getTruck().getId() != id) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        List<Cuisine> cuisines = cuisineDao.getCuisinesAlphabetical();
        model.addAttribute("cuisines", cuisines);
        model.addAttribute("menuItem", new Menu());
        model.addAttribute("truck", truckDao.getTruckById(id));
        model.addAttribute("user", user);
        return "truck/editMenu";
    }

    @PostMapping("/truck/addMenuItem")
    public String postAddMenuItem(@ModelAttribute Menu menuItem) {
        List<Menu> menuItems = new ArrayList<>();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Truck truck = truckDao.getById(user.getTruck().getId());
        menuItem.setTruck(truck);
        menuItems.add(menuItem);
        truck.setMenu(menuItems);
        truckDao.save(truck);
        return "redirect:/truck/" + truck.getId() + "/editmenu";
    }

    @PostMapping("/truck/{id}/editMenuItem")
    public String editMenuItem(@PathVariable Long id, @RequestParam(name = "menuItemId") Long menuItemId, @RequestParam(name = "menuItemName") String menuItemName, @RequestParam(name = "menuItemPrice") double menuItemPrice, @RequestParam(name = "menuItemDescription", required = false) String menuItemDescription, @RequestParam(name = "menuItemVegan", required = false) String menuItemVegan, @RequestParam(name = "menuItemVegetarian", required = false) String menuItemVegetarian) {
        Menu menuItem = menuDao.getById(menuItemId);
        menuItem.setName(menuItemName);
        menuItem.setPrice(menuItemPrice);
        if (menuItemDescription != null) {
            menuItem.setDescription(menuItemDescription);
        }
        menuItem.setVegan(menuItemVegan != null);
        menuItem.setVegetarian(menuItemVegetarian != null);
        menuDao.save(menuItem);
        return "redirect:/truck/" + id + "/editmenu";
    }

    @PostMapping("/truck/{id}/deleteMenuItem")
    public String deleteMenuItem(@PathVariable Long id, @RequestParam(name = "menuItemId") Long menuItemId) {
        menuDao.deleteById(menuItemId);
        System.out.println("aaaaa");
        return "redirect:/truck/" + id + "/editmenu";
    }

    @PostMapping("/truck/pickCuisine")
    public String pickedCuisine(@RequestParam(name = "cuisineList") String cuisineList, @RequestParam(name = "truck") Truck truck) {
        List<Cuisine> newCuisine = new ArrayList<>();
        String[] list = cuisineList.split(",");
        for (String cuisine : list) {
            newCuisine.add(cuisineDao.getById(Long.valueOf(cuisine)));
        }
        truck.setCuisines(newCuisine);
        truckDao.save(truck);
        return "redirect:/truck/" + truck.getId() + "/edit";
    }

    @GetMapping("/truck/{id}/show")
    public String showTruck(@PathVariable Long id, Model model) {
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User loggedUser = userDao.getById(user.getId());
            model.addAttribute("loggedUser", loggedUser);
        }
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