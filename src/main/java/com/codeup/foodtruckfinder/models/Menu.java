package com.codeup.foodtruckfinder.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table (name="menu_items")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="truck_id")
    private Truck truck;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private Boolean vegetarian;

    @Column(nullable = true)
    private Boolean vegan;

    @Column(nullable = false)
    private double price;

    @Column(nullable = true)
    private String description;

    public Menu() {
    }

    public Menu(long id, Truck truck, String name, Boolean vegetarian, Boolean vegan, double price, String description) {
        this.id = id;
        this.truck = truck;
        this.name = name;
        this.vegetarian = vegetarian;
        this.vegan = vegan;
        this.price = price;
        this.description = description;
    }

    public Menu(Truck truck, String name, Boolean vegetarian, Boolean vegan, double price, String description) {
        this.truck = truck;
        this.name = name;
        this.vegetarian = vegetarian;
        this.vegan = vegan;
        this.price = price;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(Boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public Boolean getVegan() {
        return vegan;
    }

    public void setVegan(Boolean vegan) {
        this.vegan = vegan;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
