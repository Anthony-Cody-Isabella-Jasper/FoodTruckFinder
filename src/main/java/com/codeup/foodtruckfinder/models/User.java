package com.codeup.foodtruckfinder.models;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private boolean truckOwner;
    @Column(nullable = false)
    private boolean admin;
    @Column
    private String profilePicture;
    @Column
    private String location;

    public User() {
    }

    public User(String username, String password, String email, boolean truckOwner, String profilePicture) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.truckOwner = truckOwner;
        this.admin = false;
        this.profilePicture = profilePicture;
    }

    public User(long id, String username, String password, String email, boolean truckOwner, String profilePicture, String location) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.truckOwner = truckOwner;
        this.admin = false;
        this.profilePicture = profilePicture;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isTruckOwner() {
        return truckOwner;
    }

    public void setTruckOwner(boolean truckOwner) {
        this.truckOwner = truckOwner;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}