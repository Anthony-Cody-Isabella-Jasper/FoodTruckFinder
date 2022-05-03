package com.codeup.foodtruckfinder.models;

import javax.persistence.*;

@Entity
@Table (name="reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Truck truck;

    @OneToOne
    private User user;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = true)
    private String review;


    public Review() {
    }


    public Review(long id, Truck truck, User user, int rating, String review) {
        this.id = id;
        this.truck = truck;
        this.user = user;
        this.rating = rating;
        this.review = review;
    }

    public Review(Truck truck, User user, int rating, String review) {
        this.truck = truck;
        this.user = user;
        this.rating = rating;
        this.review = review;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
