package com.example.ecommerceapi.service;

import com.example.ecommerceapi.api.model.User;
import com.example.ecommerceapi.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.*;

/**
 * Service class for handling product operations.
 * This class provides CRUD (Create, Read, Update, Delete) operations for users.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
@Service
public class UserService {

    /**
     * The repository for user data.
     */
    private final UserRepository myUserRepository;

    /**
     * Constructs a UserService with the provided user repository.
     *
     *
     * @param theUserRepository The user repository to be used by this service
     */
    @Autowired
    public UserService(final UserRepository theUserRepository) {
        this.myUserRepository = theUserRepository;
    }

    /**
     * Adds a new user to the repository.
     *
     * @param theUser (The user to be added)
     * @return The added user
     * @throws IllegalArgumentException If user is null
     */
    public User addUser(final User theUser) { // Create
        if (theUser == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        return myUserRepository.save(theUser);
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return A list of all users
     */
    public List<User> getAllUsers() { // Read
        return myUserRepository.findAll();
    }

    /**
     * Retrieves a user based on their unique ID.
     *
     * @param theID (User's ID)
     * @return An Optional containing the user if found, or empty otherwise
     */
    public Optional<User> getUserByID(final Long theID) { // Read
        return myUserRepository.findById(theID);
    }

    /**
     * Updates the details of an existing user.
     *
     * @param theUserID (The ID of the user to update)
     * @param theUserDetails (The user details to be updated)
     * @return The updated user.
     * @throws ResponseStatusException If the user with the given ID cannot be found.
     */
    public User updateUser(final Long theUserID, final User theUserDetails) { // Update
        if (theUserDetails == null) {
            throw new IllegalArgumentException("User details cannot be null");
        }
        if (theUserDetails.getUsername() == null || theUserDetails.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (theUserDetails.getPassword() == null || theUserDetails.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (theUserDetails.getEmail() == null || theUserDetails.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        final User user = myUserRepository.findById(theUserID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + theUserID + " not found"));

        if (theUserDetails.getUsername() != null) {
            user.setUsername(theUserDetails.getUsername());
        }
        if (theUserDetails.getPassword() != null) {
            user.setPassword(theUserDetails.getPassword());

        }
        if (theUserDetails.getEmail() != null) {

            user.setEmail(theUserDetails.getEmail());
        }

        // can add more logic to change users here

        return myUserRepository.save(user);
    }

    /**
     * Deletes a user from the repository by their ID.
     *
     * @param theUserID (The ID of the user to be deleted)
     * @throws ResponseStatusException If the user cannot be found with the given ID cannot be found
     */
    public void deleteUser(final Long theUserID) { // Delete
        final User user = myUserRepository.findById(theUserID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + theUserID + " not found"));

        myUserRepository.delete(user);
    }
}
