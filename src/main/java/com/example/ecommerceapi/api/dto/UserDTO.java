package com.example.ecommerceapi.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for user data.
 *
 * This class is used to transfer user data between different layers of the application,
 * particularly between the persistence layer and the client-facing controller. It includes
 * validation annotations to ensure that user data meets specific requirements before
 * being processed or persisted.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
public class UserDTO { // Not needed for a personal project, but good practice

    /**
     * The unique identifier of the user.
     */
    private Long myUserID;

    /**
     * The username of the user.
     * It must not be blank, and must be between 5 and 30 characters long.
     */
    @NotBlank(message = "Username is required")
    @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters")
    private String myUsername;

    /**
     * The password of the user.
     * It must not be blank, and must be between 5 and 30 characters long.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 5, max = 30, message = "Password must be between 5 and 30 characters")
    private String myPassword;

    /**
     * The email of the user.
     * It must not be blank and must be a valid email format.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String myEmail;

    /**
     * Gets the unique identifier for the user.
     *
     * @return The user's unique identifier.
     */
    public Long getUserID() {
        return myUserID;
    }

    /**
     * Sets the unique identifier for the user.
     *
     * @param theUserID The new unique identifier for the user.
     */
    public void setUserID(final Long theUserID) {
        this.myUserID = theUserID;
    }

    /**
     * Gets the username of the user.
     *
     * @return The user's username.
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
     * @return The user's password.
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
     * Gets the email of the user.
     *
     * @return The user's email.
     */
    public String getEmail() {
        return myEmail;
    }

    /**
     * Sets the email of the user.
     *
     * @param theEmail The new email for the user.
     */
    public void setEmail(final String theEmail) {
        this.myEmail = theEmail;
    }
}
