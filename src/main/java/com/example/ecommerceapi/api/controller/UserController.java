package com.example.ecommerceapi.api.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.ecommerceapi.service.UserService;
import com.example.ecommerceapi.api.model.User;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.*;

/**
 * REST controller for managing users.
 *
 * This controller provides endpoints for CRUD (Create, Read, Update, Delete) operations on user entities,
 * allowing for the management of user data within the e-commerce system. It interacts with the UserService
 * to perform business logic and data persistence.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * Service for handling user-related operations.
     */
    private final UserService myUserService;

    /**
     * Constructs a UserController with dependency injection of the UserService.
     *
     * @param theUserService (The service handling user-related business logic)
     */
    @Autowired
    public UserController(final UserService theUserService) {
        this.myUserService = theUserService;
    }

    /**
     * Retrieves a list of all users.
     *
     * @return A list of User objects.
     */
    @GetMapping
    public List<User> getAllUsers() {
        return myUserService.getAllUsers();
    }

    /**
     * Retrieves a specific user by their unique identifier.
     *
     * @param theUserID (ID of the user to retrieve)
     * @return The User object if found
     * @throws ResponseStatusException If no user with the given ID is found
     */
    @GetMapping("/{theUserID}")
    public User getUserByID(@PathVariable final Long theUserID) {
        return myUserService.getUserByID(theUserID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + theUserID + " not found"));
    }

    /**
     * Creates a new user.
     *
     * @param theUser (The User object to be created)
     * @return The newly created User object
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody final User theUser) {
        return myUserService.addUser(theUser);
    }

    /**
     * Updates an existing user's information.
     *
     * @param theUserID (The ID of the user to update)
     * @param theUserDetails (The User object containing updated information)
     * @return The updated User object.
     */
    @PutMapping("/{theUserID}")
    public User updateUser(@PathVariable Long theUserID, @RequestBody final User theUserDetails) {
        return myUserService.updateUser(theUserID, theUserDetails);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param theUserID (The ID of the user to be deleted)
     */
    @DeleteMapping("/{theUserID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable final Long theUserID) {
        myUserService.deleteUser(theUserID);
    }
}
