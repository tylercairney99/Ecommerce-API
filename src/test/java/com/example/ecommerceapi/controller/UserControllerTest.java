package com.example.ecommerceapi.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.ecommerceapi.config.RestExceptionHandler;
import com.example.ecommerceapi.api.model.User;
import com.example.ecommerceapi.api.dto.UserDTO;
import com.example.ecommerceapi.service.UserService;
import com.example.ecommerceapi.api.controller.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.modelmapper.ModelMapper;
import com.fasterxml.jackson.databind.ObjectMapper; // Import Jackson ObjectMapper for JSON conversion
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

/**
 * This class contains test methods for the UserController class to test various CRUD operations.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    /**
     * The MockMvc instance for performing HTTP request and response simulations.
     */
    private MockMvc myMockMvc;

    /**
     * A mock instance of the UserService class used for testing purposes.
     */
    @Mock
    private UserService myUserService;

    /**
     * The UserController instance with the injected mock UserService.
     */
    @InjectMocks
    private UserController myUserController;

    /**
     * A sample UserDTO used for test cases.
     */
    private UserDTO myUserDTO;

    /**
     * A sample User instance used for test cases.
     */
    private User myUser;

    /**
     * The ModelMapper instance used for mapping User to UserDTO and vice versa.
     */
    private ModelMapper myModelMapper;

    /**
     * Initializes the ModelMapper and UserController, and sets up the MockMvc for testing.
     */
    @BeforeEach
    public void setUp() {
        myModelMapper = new ModelMapper();
        myUserController = new UserController(myUserService, myModelMapper);
        myMockMvc = MockMvcBuilders.standaloneSetup(myUserController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        // Initialize a User and UserDTO
        myUser = new User("username123", "password123", "test@gmail.com");
        myUserDTO = myModelMapper.map(myUser, UserDTO.class);

    }

    /**
     * Tests retrieving all users and expects a list of users in the response.
     */
    @Test
    public void whenGetAllUsers_thenReturnUserList() throws Exception {
        // Given: Mock the UserService to return a list containing a sample user.
        given(myUserService.getAllUsers()).willReturn(Collections.singletonList(myUser));

        // When: Perform a GET request to "/users" using MockMvc and make assertions.
        myMockMvc.perform(get("/users"))
                .andExpect(status().isOk()) // Expecting a 200 OK status code.
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Expecting the response content type to be JSON.
                .andExpect(jsonPath("$", hasSize(1))) // Expecting the JSON response to be an array with a single element (the sample user).
                .andExpect(jsonPath("$[0].username", is(myUserDTO.getUsername()))); // Expecting the username of the first element in
                                                                                             // the array to match the sample user's username.

    }

    /**
     * Tests retrieving a user by ID and expects a user DTO in the response.
     */
    @Test
    public void whenGetUserByID_thenReturnUserDTO() throws Exception {
        final Long userID = 1L;
        given(myUserService.getUserByID(userID)).willReturn(Optional.of(myUser));

        myMockMvc.perform(get("/users/" + userID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(myUserDTO.getUsername())));
    }

    /**
     * Tests adding a user and expects the saved user DTO in the response.
     */
    @Test
    public void whenAddUser_thenReturnSavedUserDTO() throws Exception {
        given(myUserService.addUser(org.mockito.ArgumentMatchers.any(User.class))).willReturn(myUser);

        final ObjectMapper objectMapper = new ObjectMapper();
        final String userJson = objectMapper.writeValueAsString(myUserDTO);

        myMockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(myUserDTO.getUsername())));
    }

    /**
     * Tests updating a user and expects the updated user DTO in the response.
     */
    @Test
    public void whenUpdateUser_thenReturnUpdatedUserDTO() throws Exception {
        final Long userID = 1L;
        given(myUserService.updateUser(eq(userID), org.mockito.ArgumentMatchers.any(User.class))).willReturn(myUser);


        final ObjectMapper objectMapper = new ObjectMapper();
        final String userJson = objectMapper.writeValueAsString(myUserDTO);

        myMockMvc.perform(put("/users/" + userID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(myUserDTO.getUsername())));
    }

    /**
     * Tests deleting a user and expects a No Content response.
     */
    @Test
    public void whenDeleteUser_thenStatusNoContent() throws Exception {
        final Long userID = 1L;
        willDoNothing().given(myUserService).deleteUser(userID);

        myMockMvc.perform(delete("/users/" + userID))
                .andExpect(status().isNoContent());
    }

    /**
     * Tests retrieving a user by a non-existent ID and expects a Not Found response.
     */
    @Test
    public void whenGetUserByNonExistentID_thenThrowNotFoundException() throws Exception {
        final Long nonExistentUserID = 999L;
        given(myUserService.getUserByID(nonExistentUserID)).willReturn(Optional.empty());

        myMockMvc.perform(get("/users/" + nonExistentUserID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"User with ID " + nonExistentUserID + " not found\"",
                        result.getResolvedException().getMessage()));
    }

    /**
     * Tests adding a user with invalid data and expects a Bad Request response.
     */
    @Test
    public void whenAddUserWithInvalidData_thenBadRequest() throws Exception {
        final UserDTO invalidUserDTO = new UserDTO();
        final String userJson = new ObjectMapper().writeValueAsString(invalidUserDTO);

        myMockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                        .andExpect(status().isBadRequest());
    }

    /**
     * Tests updating a user with invalid data and expects a Bad Request response.
     */
    @Test
    public void whenUpdateUserWithInvalidData_thenBadRequest() throws Exception {
        final Long userID = 1L;
        final UserDTO invalidUserDTO = new UserDTO(); // Invalid DTO
        final String userJson = new ObjectMapper().writeValueAsString(invalidUserDTO);

        myMockMvc.perform(put("/users/" + userID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                        .andExpect(status().isBadRequest());
    }

    /**
     * Tests retrieving a user with an invalid ID type (non-numeric) and expects a Bad Request response.
     */
    @Test
    public void whenGetUserByInvalidIDType_thenBadRequest() throws Exception {
        myMockMvc.perform(get("/users/abc")) // Non-numeric ID
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests deleting a user with an invalid ID type (non-numeric) and expects a Bad Request response.
     */
    @Test
    public void whenDeleteUserByInvalidIDType_thenBadRequest() throws Exception {
        myMockMvc.perform(delete("/users/abc")) // Non-numeric ID
                .andExpect(status().isBadRequest());
    }

}
