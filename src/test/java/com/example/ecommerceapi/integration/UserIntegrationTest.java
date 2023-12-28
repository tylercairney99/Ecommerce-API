package com.example.ecommerceapi.integration;

import com.example.ecommerceapi.api.dto.ProductDTO;
import com.example.ecommerceapi.api.dto.UserDTO;
import com.example.ecommerceapi.api.model.Product;
import com.example.ecommerceapi.api.model.User;
import com.example.ecommerceapi.api.repository.OrderLineRepository;
import com.example.ecommerceapi.api.repository.OrderRepository;
import com.example.ecommerceapi.api.repository.ProductRepository;
import com.example.ecommerceapi.api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest // Indicates the test should load the entire application for a more comprehensive integration test
@AutoConfigureMockMvc // Automatically configures myMockMvc for sending HTTP requests to controllers
public class UserIntegrationTest {

    /**
     * MockMvc provides support for Spring MVC testing. It allows us to send HTTP requests
     * and assert the response status and content for integration testing.
     */
    @Autowired // Injects the MockMvc instance provided by Spring for simulating HTTP requests
    private MockMvc myMockMvc;

    /**
     * ObjectMapper is used for mapping JSON data to and from POJO (Plain Old Java Object)
     * for the purposes of testing REST controllers.
     */
    @Autowired // Injects the ObjectMapper instance for JSON serialization/deserialization
    private ObjectMapper myObjectMapper;

    /**
     * UserRepository is the JPA repository for product entities. Used here to
     * directly interact with the database for test setup and verification.
     */
    @Autowired // Injects the UserRepository for database operations related to 'User'
    private UserRepository myUserRepository;

    /**
     * OrderLineRepository is the JPA repository for order line entities. Used here to
     * clear any dependencies before deleting products in the setup phase.
     */
    @Autowired // Executed before each test. It clears the data from 'order_lines' and 'products' tables
    private OrderLineRepository myOrderLineRepository;

    /**
     * OrderRepository is the JPA repository for order entities. Used here to
     * directly interact with the database for test setup and verification.
     */
    @Autowired // Injects the OrderRepository for database operations related to 'Order'
    private OrderRepository myOrderRepository;

    /**
     * Set up method executed before each test. It ensures that the database is in a
     * known state by clearing the order lines and products tables. This is crucial for
     * maintaining test isolation and ensuring that tests do not interfere with each other.
     */
    @BeforeEach
    public void setUp() {
        myOrderRepository.deleteAll(); // Clear orders before products
        myOrderLineRepository.deleteAll(); // Clear order lines before products
        myUserRepository.deleteAll(); // Now it's safe to delete all products
    }

    /**
     * Tests creating a new user. Verifies if the user is successfully created
     * and returns the expected status and data.
     */
    @Test
    @Transactional // Ensures that database operations in the test are rolled back after the test
    public void whenAddUser_thenUserIsCreated() throws Exception {
        final UserDTO userDTO = new UserDTO(null, "Test Username", "Test Password", "test@gmail.com");

        myMockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(myObjectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Test Username"));
    }

    /**
     * Tests retrieving all users. Verifies if the response contains the correct
     * product data and status.
     */
    @Test
    @Transactional
    public void whenGetAllUsers_thenUsersAreReturned() throws Exception {
        final User savedUser = myUserRepository.save(new User("Test Username",
                "Test Password", "test@gmail.com"));

        myMockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value(savedUser.getUsername()));
    }

    /**
     * Tests updating an existing user. Verifies if the user is successfully
     * updated and returns the correct status and updated data.
     */
    @Test
    @Transactional
    public void whenUpdateUser_thenUserIsUpdated() throws Exception {
        final User savedUser = myUserRepository.save(new User("Test Username",
                "Test Password", "test@gmail.com"));
        final UserDTO updatedUserDTO = new UserDTO(savedUser.getUserID(),"Test Username2",
                "Test Password2", "test@gmail2.com");

        myMockMvc.perform(put("/users/" + savedUser.getUserID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(myObjectMapper.writeValueAsString(updatedUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Test Username2"));
    }

    /**
     * Tests deleting an existing user. Verifies if the user is successfully
     * deleted and returns the expected status.
     */
    @Test
    @Transactional
    public void whenDeleteUser_thenUserIsDeleted() throws Exception {
        final User savedUser = myUserRepository.save(new User("Test Username",
                "Test Password", "test@gmail.com"));

        myMockMvc.perform(delete("/users/" + savedUser.getUserID()))
                .andExpect(status().isNoContent());

        assertFalse(myUserRepository.findById(savedUser.getUserID()).isPresent(),"User should be deleted");
    }

    /**
     * Tests retrieving a specific users by their ID. Verifies if the correct user
     * is returned with the expected data.
     */
    @Test
    @Transactional
    public void whenGetUserByID_thenUserIsReturned() throws Exception {
        final User savedUser = myUserRepository.save(new User("Test Username",
                "Test Password", "test@gmail.com"));

        myMockMvc.perform(get("/users/" + savedUser.getUserID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(savedUser.getUsername()));
    }

    /**
     * Tests retrieving a non-existent user by their ID. Verifies if the response
     * returns a Not Found status.
     */
    @Test
    @Transactional
    public void whenGetNonExistentUser_thenNotFound() throws Exception {
        final Long nonExistentUserID = 999L;

        myMockMvc.perform(get("/users/" + nonExistentUserID))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests updating a non-existent users. Verifies if the response returns
     * a Not Found status when trying to update a user that does not exist.
     */
    @Test
    @Transactional
    public void whenUpdateNonExistentUser_thenNotFound() throws Exception {
        final UserDTO updatedUserDTO = new UserDTO(999L, "Test Username",
                "Test Password", "test@gmail.com");

        myMockMvc.perform(put("/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(myObjectMapper.writeValueAsString(updatedUserDTO)))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests deleting a non-existent user. Verifies if the response returns
     * a Not Found status when trying to delete a user that does not exist.
     */
    @Test
    @Transactional
    public void whenDeleteNonExistentUser_thenNotFound() throws Exception {
        final Long nonExistentUserID = 999L;

        myMockMvc.perform(delete("/users/" + nonExistentUserID))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests adding a user with invalid data. Verifies if the response returns
     * a Bad Request status when user data does not meet validation criteria.
     */
    @Test
    @Transactional
    public void whenAddUserWithInvalidData_thenBadRequest() throws Exception {
        final UserDTO invalidUserDTO = new UserDTO(null, "", "", "");

        myMockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(myObjectMapper.writeValueAsString(invalidUserDTO)))
                .andExpect(status().isBadRequest());
    }


}
