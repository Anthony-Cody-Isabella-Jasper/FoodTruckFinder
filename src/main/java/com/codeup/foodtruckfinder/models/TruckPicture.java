package com.codeup.foodtruckfinder.models;

import javax.persistence.*;

@Entity
@Table(name = "truck_pictures")
public class TruckPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String picture;
    @ManyToOne
    @JoinColumn(name = "truck_id")
    private Truck truck;

    public TruckPicture() {
    }

    public TruckPicture(String picture, Truck truck) {
        this.picture = picture;
        this.truck = truck;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }
}