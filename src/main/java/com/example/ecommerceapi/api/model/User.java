package com.example.ecommerceapi.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long userID;

    private String username;

    private String password;

    private String email;

    public User() {
        // no arg constructor for JPA
    }

    public User(final String theUsername, final String thePassword, final String theEmail) {
        this.username = theUsername;
        this.password = thePassword;
        this.email = theEmail;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(final Long theID) {
        this.userID = theID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String theUsername) {
        this.username = theUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String thePassword) {
        this.password = thePassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String theEmail) {
        this.email = theEmail;
    }

}
