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
     */
    public User addUser(final User theUser) {
        return myUserRepository.save(theUser);
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return A list of all users
     */
    public List<User> getAllUsers() {
        return myUserRepository.findAll();
    }

    /**
     * Retrieves a user based on their unique ID.
     *
     * @param theID (User's ID)
     * @return An Optional containing the user if found, or empty otherwise
     */
    public Optional<User> getUserByID(final Long theID) {
        return myUserRepository.findById(theID);
    }

    /**
     * Updates the details of an existing user.
     *
     * @param theID (The ID of the user to update)
     * @param userDetails (The user details to be updated)
     * @return The updated user.
     * @throws ResponseStatusException If the user with the given ID cannot be found.
     */
    public User updateUser(final Long theID, final User userDetails) {
        final User user = myUserRepository.findById(theID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + theID + " not found"));

        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setEmail(userDetails.getEmail());

        // can add more logic to change users here

        return myUserRepository.save(user);
    }

    /**
     * Deletes a user from the repository by their ID.
     *
     * @param theID (The ID of the user to be deleted)
     * @throws ResponseStatusException If the user cannot be found.
     */
    public void deleteUser(final Long theID) {
        final User user = myUserRepository.findById(theID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + theID + " not found"));

        myUserRepository.delete(user);
    }
}
