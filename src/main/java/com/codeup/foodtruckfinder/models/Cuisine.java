package com.codeup.foodtruckfinder.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cuisines")
public class Cuisine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @ManyToMany(mappedBy = "cuisines")
    private List<Truck> trucksWithCuisine;

    public Cuisine() {
    }

    public Cuisine(long id, String name, List<Truck> trucksWithCuisine) {
        this.id = id;
        this.name = name;
        this.trucksWithCuisine = trucksWithCuisine;
    }

    public Cuisine(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Truck> getTrucksWithCuisine() {
        return trucksWithCuisine;
    }

    public void setTrucksWithCuisine(List<Truck> trucksWithCuisine) {
        this.trucksWithCuisine = trucksWithCuisine;
    }
}