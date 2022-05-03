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
    private String reviewText;


    public Review() {
    }


    public Review(long id, Truck truck, User user, int rating, String reviewText) {
        this.id = id;
        this.truck = truck;
        this.user = user;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    public Review(Truck truck, User user, int rating, String reviewText) {
        this.truck = truck;
        this.user = user;
        this.rating = rating;
        this.reviewText = reviewText;
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

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
