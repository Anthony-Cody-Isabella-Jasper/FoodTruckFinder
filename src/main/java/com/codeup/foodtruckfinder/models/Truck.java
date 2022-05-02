package com.codeup.foodtruckfinder.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name="truck_profiles")
public class Truck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private User truck_owner;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true, unique = true)
    private String phone;

    @Column(nullable = true)
    private String profile_picture;

    @Column(nullable = true)
    private String location;

    @Column(nullable = false)
    private Boolean location_confirmation;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "truck")
    private List<Menu> menu;

    public Truck() {
    }

    public Truck(long id, User truck_owner, String name, String description, String phone, String profile_picture, String location, Boolean location_confirmation) {
        this.id = id;
        this.truck_owner = truck_owner;
        this.name = name;
        this.description = description;
        this.phone = phone;
        this.profile_picture = profile_picture;
        this.location = location;
        this.location_confirmation = location_confirmation;
    }


    public Truck(String name, String description, String phone, String profile_picture) {
        this.name = name;
        this.description = description;
        this.phone = phone;
        this.profile_picture = profile_picture;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getTruck_owner() {
        return truck_owner;
    }

    public void setTruck_owner(User truck_owner) {
        this.truck_owner = truck_owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getLocation_confirmation() {
        return location_confirmation;
    }

    public void setLocation_confirmation(Boolean location_confirmation) {
        this.location_confirmation = location_confirmation;
    }
}
