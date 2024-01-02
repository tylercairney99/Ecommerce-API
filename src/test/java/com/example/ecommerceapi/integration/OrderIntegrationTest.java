package com.example.ecommerceapi.integration;

import com.example.ecommerceapi.api.dto.OrderDTO;
import com.example.ecommerceapi.api.dto.UserDTO;
import com.example.ecommerceapi.api.model.Order;
import com.example.ecommerceapi.api.model.OrderLine;
import com.example.ecommerceapi.api.model.Product;
import com.example.ecommerceapi.api.model.User;
import com.example.ecommerceapi.api.dto.OrderLineDTO;
import com.example.ecommerceapi.api.repository.OrderLineRepository;
import com.example.ecommerceapi.api.repository.OrderRepository;
import com.example.ecommerceapi.api.repository.ProductRepository;
import com.example.ecommerceapi.api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
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
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest // Indicates the test should load the entire application for a more comprehensive integration test
@AutoConfigureMockMvc // Automatically configures myMockMvc for sending HTTP requests to controllers
public class OrderIntegrationTest {

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
     * OrderRepository is the JPA repository for order entities. Used here to
     * directly interact with the database for test setup and verification.
     */
    @Autowired // Injects the OrderRepository for database operations related to 'Order'
    private OrderRepository myOrderRepository;

    /**
     * OrderLineRepository is the JPA repository for order line entities. Used here to
     * clear any dependencies before deleting products in the setup phase.
     */
    @Autowired // Executed before each test. It clears the data from 'order_lines' and 'products' tables
    private OrderLineRepository myOrderLineRepository;

    /**
     * ProductRepository is the JPA repository for product entities. Used here to
     * directly interact with the database for test setup and verification.
     */
    @Autowired // Injects the ProductRepository for database operations related to 'Product'
    private ProductRepository myProductRepository;

    /**
     * UserRepository is the JPA repository for user entities. Used here to
     * directly interact with the database for test setup and verification.
     */
    @Autowired // Injects the UserRepository for database operations related to 'User'
    private UserRepository myUserRepository;


    private User myTestUser;

    private Product myTestProduct;

    /**
     * Set up method executed before each test. It ensures that the database is in a
     * known state by clearing the order lines and products tables. This is crucial for
     * maintaining test isolation and ensuring that tests do not interfere with each other.
     */
    @BeforeEach
    public void setUp() {
//        myProductRepository.deleteAll(); // Clear orders before products
//        myOrderLineRepository.deleteAll(); // Clear order lines before products
        myOrderRepository.deleteAll(); // Clear orders before products

        myTestUser = myUserRepository.save(new User("TestUser", "Password123", "testuser@example.com"));
        myTestProduct = myProductRepository.save(new Product("TestProduct", new BigDecimal("20.00"), 100));
    }

    /**
     * Tests creating a new order. Verifies if the order is successfully created
     * and returns the expected status and data.
     */
    @Test
    @Transactional
    public void whenAddOrder_thenOrderIsCreated() throws Exception {
        OrderLineDTO orderLineDTO = new OrderLineDTO(myTestProduct.getID(), 2, myTestProduct.getPrice());
        OrderDTO orderDTO = new OrderDTO(null, myTestUser.getUserID(), Collections.singletonList(orderLineDTO),
                LocalDateTime.now(), new BigDecimal("40.00"));

        myMockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(myObjectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userID").value(myTestUser.getUserID()))
                .andExpect(jsonPath("$.orderLines[0].productId").value(myTestProduct.getID()))
                .andExpect(jsonPath("$.orderLines[0].quantity").value(2))
                .andExpect(jsonPath("$.orderLines[0].unitPrice").value(20.00))
                .andExpect(jsonPath("$.orderLines[0].lineTotal").value(40.00))
                .andExpect(jsonPath("$.totalAmount").value(40.00))
                .andExpect(jsonPath("$.orderID").exists());
    }

    @Test
    @Transactional
    public void whenGetAllOrders_thenOrdersAreReturned() throws Exception {
        // Create an OrderLineDTO
        OrderLineDTO orderLineDTO = new OrderLineDTO(myTestProduct.getID(), 2, myTestProduct.getPrice());

        // Create an OrderDTO with the OrderLineDTO
        OrderDTO orderDTO = new OrderDTO(null, myTestUser.getUserID(), Collections.singletonList(orderLineDTO),
                LocalDateTime.now(), new BigDecimal("40.00"));

        // Use MockMvc to make a POST request to create the order
        myMockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(myObjectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isCreated());

        // Now retrieve all orders and perform assertions
        myMockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderID").exists())
                .andExpect(jsonPath("$[0].userID").value(myTestUser.getUserID()));
    }

    @Test
    @Transactional
    public void whenUpdateOrder_thenOrderIsUpdated() throws Exception {
        // Create and save a test order
        OrderLineDTO initialOrderLineDTO = new OrderLineDTO(myTestProduct.getID(), 2, myTestProduct.getPrice());
        OrderDTO initialOrderDTO = new OrderDTO(null, myTestUser.getUserID(), Collections.singletonList(initialOrderLineDTO),
                LocalDateTime.now(), new BigDecimal("40.00"));

        MvcResult result = myMockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(myObjectMapper.writeValueAsString(initialOrderDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Long orderId = JsonPath.parse(response).read("$.orderID", Long.class);

        // Update the order
        OrderLineDTO updatedOrderLineDTO = new OrderLineDTO(myTestProduct.getID(), 3, myTestProduct.getPrice());
        OrderDTO updatedOrderDTO = new OrderDTO(orderId, myTestUser.getUserID(), Collections.singletonList(updatedOrderLineDTO),
                LocalDateTime.now(), new BigDecimal("60.00"));

        myMockMvc.perform(put("/orders/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(myObjectMapper.writeValueAsString(updatedOrderDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderID").value(orderId))
                .andExpect(jsonPath("$.totalAmount").value(60.00));
    }

    @Test
    @Transactional
    public void whenDeleteOrder_thenOrderIsDeleted() throws Exception {
        // Create and save a test order
        Order savedOrder = new Order(LocalDateTime.now(), "CREATED", new BigDecimal("40.00"), myTestUser);
        savedOrder = myOrderRepository.save(savedOrder);

        // Use MockMvc to send a DELETE request
        myMockMvc.perform(delete("/orders/" + savedOrder.getOrderID()))
                .andExpect(status().isNoContent());

        // Verify that the order is deleted from the repository
        assertFalse(myOrderRepository.findById(savedOrder.getOrderID()).isPresent(), "Order should be deleted");
    }

    @Test
    @Transactional
    public void whenGetOrderByID_thenOrderIsReturned() throws Exception {
        // Create and save a test order
        Order savedOrder = new Order(LocalDateTime.now(), "CREATED", new BigDecimal("40.00"), myTestUser);
        savedOrder = myOrderRepository.save(savedOrder);

        // Use MockMvc to send a GET request
        myMockMvc.perform(get("/orders/" + savedOrder.getOrderID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderID").value(savedOrder.getOrderID()));
    }

    @Test
    @Transactional
    public void whenGetNonExistentOrder_thenNotFound() throws Exception {
        final Long nonExistentOrderID = 999L;

        // Use MockMvc to send a GET request for a non-existent order
        myMockMvc.perform(get("/orders/" + nonExistentOrderID))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void whenUpdateNonExistentOrder_thenNotFound() throws Exception {
        final OrderDTO updatedOrderDTO = new OrderDTO(999L, myTestUser.getUserID(),
                Collections.emptyList(), LocalDateTime.now(), new BigDecimal("50.00"));

        // Use MockMvc to send a PUT request for a non-existent order
        myMockMvc.perform(put("/orders/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(myObjectMapper.writeValueAsString(updatedOrderDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void whenDeleteNonExistentOrder_thenNotFound() throws Exception {
        final Long nonExistentOrderID = 999L;

        // Use MockMvc to send a DELETE request for a non-existent order
        myMockMvc.perform(delete("/orders/" + nonExistentOrderID))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void whenAddOrderWithInvalidData_thenBadRequest() throws Exception {
        // Construct an OrderLineDTO with invalid data (e.g., null product ID, negative quantity)
        OrderLineDTO invalidOrderLineDTO = new OrderLineDTO(null, -1, new BigDecimal("10.00"));

        // Construct an OrderDTO with invalid data (e.g., null user ID, negative total amount)
        OrderDTO invalidOrderDTO = new OrderDTO(null, null, Collections.singletonList(invalidOrderLineDTO),
                LocalDateTime.now(), new BigDecimal("-100.00"));

        // Use MockMvc to send a POST request with the invalid order data
        myMockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(myObjectMapper.writeValueAsString(invalidOrderDTO)))
                .andExpect(status().isBadRequest());
    }










}
