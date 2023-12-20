package com.example.ecommerceapi.api.model;

import jakarta.persistence.*;

/**
 * Represents a user entity in the e-commerce system.
 * This class maps to a database table and provides data fields for user attributes.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
@Entity // Marks this class as a JPA entity (i.e., a persistent domain object).
@Table(name = "users") // Specifies the table in the database with which this entity is associated.
public class User {

    @Id  // Marks this field as the primary key of the entity.
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Configures the way of increment of the specified column(field).

    /**
     *
     */
    private Long myUserID;

    /**
     * User's username.
     */
    private String myUsername;

    /**
     * User's password.
     */
    private String myPassword;

    /**
     * User's email.
     */
    private String myEmail;

    /**
     * Default constructor for JPA.
     * This is required for JPA entity initialization.
     */
    public User() {
        // no arg constructor for JPA
    }

    /**
     * Constructs a new User with the specified username, password, and email.
     *
     * @param theUsername The username of the user.
     * @param thePassword The password of the user.
     * @param theEmail The email address of the user.
     */
    public User(final String theUsername, final String thePassword, final String theEmail) {
        this.myUsername = theUsername;
        this.myPassword = thePassword;
        this.myEmail = theEmail;
    }

    /**
     * Gets the user ID.
     *
     * @return The ID of the user.
     */
    public Long getUserID() {
        return myUserID;
    }

    /**
     * Sets the user ID.
     *
     * @param theID The new ID for the user.
     */
    public void setUserID(final Long theID) {
        this.myUserID = theID;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return myUsername;
    }

    /**
     * Sets the username of the user.
     *
     * @param theUsername The new username for the user.
     */
    public void setUsername(final String theUsername) {
        this.myUsername = theUsername;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return myPassword;
    }

    /**
     * Sets the password of the user.
     *
     * @param thePassword The new password for the user.
     */
    public void setPassword(final String thePassword) {
        this.myPassword = thePassword;
    }

    /**
     * Gets the email address of the user.
     *
     * @return The email address of the user.
     */
    public String getEmail() {
        return myEmail;
    }

    /**
     * Sets the email address of the user.
     *
     * @param theEmail The new email address for the user.
     */
    public void setEmail(final String theEmail) {
        this.myEmail = theEmail;
    }

}
