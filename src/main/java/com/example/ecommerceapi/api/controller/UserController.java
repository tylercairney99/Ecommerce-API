package com.example.ecommerceapi.api.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.ecommerceapi.service.UserService;
import com.example.ecommerceapi.api.model.User;
import com.example.ecommerceapi.api.dto.UserDTO;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.modelmapper.ModelMapper;
import jakarta.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing users.
 *
 * This controller provides endpoints for CRUD (Create, Read, Update, Delete) operations on user entities,
 * allowing for the management of user data within the e-commerce system. It interacts with the UserService
 * to perform business logic and data persistence.
 *
 * @author Tyler Cairney
 * @version 1.0.1
 */
@RestController // Controller for user operations at '/users' endpoint
@RequestMapping("/users")
public class UserController {

    /**
     * Service for handling user-related operations.
     */
    private final UserService myUserService;

    /**
     * ModelMapper for converting between DTO and entity classes.
     */
    private final ModelMapper myModelMapper;

    /**
     * Constructs a UserController with dependency injection of the UserService.
     *
     * @param theUserService (The service handling user-related business logic)
     * @param theModelMapper (Mapping between DTO and entity classes)
     */
    @Autowired // Automatically injects an instance of UserService
    public UserController(final UserService theUserService, final ModelMapper theModelMapper) {
        this.myUserService = theUserService;
        this.myModelMapper = theModelMapper;
    }

    /**
     * Retrieves all users and returns them as a list of UserDTOs.
     *
     * This method fetches a list of all User entities, maps them to UserDTOs,
     * and returns the list of DTOs. Useful for providing a view of user data
     * suitable for client-side use.
     *
     * @return A list of UserDTOs representing all users.
     */
    @GetMapping // Endpoint for getting all users
    public List<UserDTO> getAllUsers() {
        final List<User> users = myUserService.getAllUsers();
        return users.stream()
                .map(user -> myModelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific user by their unique identifier and returns it as a UserDTO.
     *
     * This method fetches a user from the database using their ID. If found, the user
     * is mapped to a UserDTO and returned. If no user is found with the provided ID,
     * a ResponseStatusException with HttpStatus.NOT_FOUND is thrown.
     *
     * @param theUserID (The unique identifier of the user to be retrieved)
     * @return A UserDTO representing the retrieved user.
     * @throws ResponseStatusException If no user with the given ID is found.
     */
    @GetMapping("/{theUserID}")
    public UserDTO getUserByID(@PathVariable final Long theUserID) {
        User user = myUserService.getUserByID(theUserID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + theUserID + " not found"));
        return myModelMapper.map(user, UserDTO.class);
    }

    /**
     * Creates and adds a new user to the system.
     *
     * Accepts a UserDTO object, validates it, and maps it to a User entity
     * for creation. Returns the created user as a UserDTO.
     *
     * @param theUserDTO (The DTO containing the new user's data)
     * @return The created user as a UserDTO.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Endpoint to create a new user, returns HTTP 201 status on creation
    public UserDTO addUser(@Valid @RequestBody final UserDTO theUserDTO) {
        final User user = myModelMapper.map(theUserDTO, User.class);
        final User createdUser = myUserService.addUser(user);
        return myModelMapper.map(createdUser, UserDTO.class);
    }

    /**
     * Updates the information of an existing user.
     *
     * Accepts a UserDTO object with updated data, validates it, and maps it
     * to the User entity to update. The user to update is identified by theUserID.
     * Returns the updated user as a UserDTO.
     *
     * @param theUserID (The ID of the user to be updated)
     * @param theUserDTO (The DTO containing the user's updated data)
     * @return The updated user as a UserDTO.
     */
    @PutMapping("/{theUserID}")
    public UserDTO updateUser(@PathVariable Long theUserID, @Valid @RequestBody final UserDTO theUserDTO) {
        User userToUpdate = myModelMapper.map(theUserDTO, User.class);
        User updatedUser = myUserService.updateUser(theUserID, userToUpdate);
        return myModelMapper.map(updatedUser, UserDTO.class);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param theUserID (The ID of the user to be deleted)
     */
    @DeleteMapping("/{theUserID}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Endpoint to delete a user by their ID, returns HTTP 204 status on successful deletion
    public void deleteUser(@PathVariable final Long theUserID) {
        myUserService.deleteUser(theUserID);
    }
}
