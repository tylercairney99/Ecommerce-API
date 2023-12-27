package com.example.ecommerceapi.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.ecommerceapi.api.controller.OrderController;
import com.example.ecommerceapi.api.controller.ProductController;
import com.example.ecommerceapi.api.dto.OrderDTO;
import com.example.ecommerceapi.api.model.OrderLine;
import com.example.ecommerceapi.api.dto.OrderLineDTO;
import com.example.ecommerceapi.api.dto.ProductDTO;
import com.example.ecommerceapi.api.dto.UserDTO;
import com.example.ecommerceapi.api.model.Order;
import com.example.ecommerceapi.api.model.Product;
import com.example.ecommerceapi.api.model.User;
import com.example.ecommerceapi.config.RestExceptionHandler;
import com.example.ecommerceapi.service.OrderService;
import com.example.ecommerceapi.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * This class contains test methods for the OrderController class to test various CRUD operations.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
@ExtendWith(SpringExtension.class)
public class OrderControllerTest {

    /**
     * The MockMvc instance for performing HTTP request and response simulations.
     */
    private MockMvc myMockMvc;

    /**
     * A mock instance of the OrderService class used for testing purposes.
     */
    @Mock
    private OrderService myOrderService;

    /**
     * The OrderController instance with the injected mock OrderService.
     */
    @InjectMocks
    private OrderController myOrderController;

    /**
     * A sample OrderDTO used for test cases.
     */
    private OrderDTO myOrderDTO;

    /**
     * A sample Order instance used for test cases.
     */
    private Order myOrder;

    /**
     * The ModelMapper instance used for mapping Order to OrderDTO and vice versa.
     */
    private ModelMapper myModelMapper;

    /**
     * Initializes the ModelMapper and OrderController, and sets up the MockMvc for testing.
     */
    @BeforeEach
    public void setUp() {
        myModelMapper = new ModelMapper();
        myOrderController = new OrderController(myOrderService, myModelMapper);
        myMockMvc = MockMvcBuilders.standaloneSetup(myOrderController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User("testUsername", "testPassword", "test@example.com");

        // Initialize a Product and UserDTO
        myOrder = new Order(LocalDateTime.now(), "Pending", BigDecimal.valueOf(99.99), user);
        myOrderDTO = myModelMapper.map(myOrder, OrderDTO.class);
    }

    /**
     * Tests retrieving all orders and expects a list of orders in the response.
     */
    @Test
    public void whenGetAllOrders_thenReturnOrderList() throws Exception {
        // Given: Mock the ProductService to return a list containing a sample product.
        given(myOrderService.getAllOrders()).willReturn(Collections.singletonList(myOrder));

        // When: Perform a GET request to "/products" using MockMvc and make assertions.
        myMockMvc.perform(get("/orders"))
                .andExpect(status().isOk()) // Expecting a 200 OK status code.
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Expecting the response content type to be JSON.
                .andExpect(jsonPath("$", hasSize(1))) // Expecting the JSON response to be an array with a single element.
                .andExpect(jsonPath("$[0].orderID", is(myOrderDTO.getOrderID()))); // Adjust the JSON path to match your OrdersDTO's structure.
    }

    /**
     * Tests retrieving an order by ID and expects an order DTO in the response.
     */
    @Test
    public void whenGetOrderByID_thenReturnOrderDTO() throws Exception {
        final Long orderID = 1L;
        given(myOrderService.getOrderByID(orderID)).willReturn(Optional.of(myOrder));

        myMockMvc.perform(get("/orders/" + orderID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderID", is(myOrderDTO.getOrderID())));
    }

    /**
     * Tests adding an order and expects the saved order DTO in the response.
     */
    @Test
    public void whenAddOrder_thenReturnSavedOrderDTO() throws Exception {
        given(myOrderService.addOrder(org.mockito.ArgumentMatchers.any(Order.class))).willReturn(myOrder);

        // Initialize a valid OrderLineDTO
        OrderLineDTO validOrderLineDTO = new OrderLineDTO(1L, 3, new BigDecimal("50.00")); // Example values

        // Initialize the OrderDTO with valid values
        OrderDTO validOrderDTO = new OrderDTO();
        validOrderDTO.setOrderID(null); // Assuming this is a new order and ID is set by the system
        validOrderDTO.setUserID(1L); // Valid user ID
        validOrderDTO.setOrderLines(Arrays.asList(validOrderLineDTO)); // Valid order lines
        validOrderDTO.setOrderDate(LocalDateTime.now()); // Current date and time
        validOrderDTO.setTotalAmount(new BigDecimal("150.00")); // Assuming total amount is valid

        // Convert OrderDTO to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String orderJson = objectMapper.writeValueAsString(validOrderDTO);

        // Perform the test
        myMockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderID", is(validOrderDTO.getOrderID())));
    }
    /**
     * Tests updating an order and expects the updated order DTO in the response.
     */
    @Test
    public void whenUpdateOrder_thenReturnUpdatedOrderDTO() throws Exception {
        final Long orderID = 1L;
        // Mock an Order instance that represents the updated order
        Order updatedOrder = new Order(LocalDateTime.now(), "UpdatedStatus", BigDecimal.valueOf(150.00),
                new User("updatedUsername", "updatedPassword", "updated@example.com"));
        updatedOrder.setOrderID(orderID); // Set the same order ID as being updated

        given(myOrderService.updateOrder(eq(orderID), ArgumentMatchers.any(Order.class))).willReturn(updatedOrder);


        // Initialize a valid OrderLineDTO
        OrderLineDTO validOrderLineDTO = new OrderLineDTO(1L, 5, new BigDecimal("30.00")); // Updated values

        // Initialize the OrderDTO with values for updating
        OrderDTO validOrderDTO = new OrderDTO();
        validOrderDTO.setOrderID(orderID); // The ID of the order being updated
        validOrderDTO.setUserID(2L); // Updated user ID
        validOrderDTO.setOrderLines(Arrays.asList(validOrderLineDTO)); // Updated order lines
        validOrderDTO.setOrderDate(LocalDateTime.now()); // Updated date and time
        validOrderDTO.setTotalAmount(new BigDecimal("150.00")); // Updated total amount

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String orderJson = objectMapper.writeValueAsString(validOrderDTO);

        // Perform the test
        myMockMvc.perform(put("/orders/" + orderID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderID", is(orderID.intValue())));
    }


    /**
     * Tests deleting a order and expects a No Content response.
     */
    @Test
    public void whenDeleteOrder_thenStatusNoContent() throws Exception {
        final Long orderID = 1L;
        willDoNothing().given(myOrderService).deleteOrder(orderID);

        myMockMvc.perform(delete("/orders/" + orderID))
                .andExpect(status().isNoContent());
    }

    /**
     * Tests retrieving an order by a non-existent ID and expects a Not Found response.
     */
    @Test
    public void whenGetOrderByNonExistentID_thenThrowNotFoundException() throws Exception {
        final Long nonExistentOrderID = 999L;
        given(myOrderService.getOrderByID(nonExistentOrderID)).willReturn(Optional.empty());

        myMockMvc.perform(get("/orders/" + nonExistentOrderID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"Order with ID " + nonExistentOrderID + " not found\"",
                        result.getResolvedException().getMessage()));
    }

    /**
     * Tests adding an order with invalid data and expects a Bad Request response.
     */
    @Test
    public void whenAddOrderWithInvalidData_thenBadRequest() throws Exception {
        final OrderDTO invalidOrderDTO = new OrderDTO();
        final String orderJson = new ObjectMapper().writeValueAsString(invalidOrderDTO);

        myMockMvc.perform(post("/orders/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests order an order with invalid data and expects a Bad Request response.
     */
    @Test
    public void whenUpdateOrderWithInvalidData_thenBadRequest() throws Exception {
        final Long orderID = 1L;
        OrderDTO invalidOrderDTO = new OrderDTO(); // Create an invalid OrderDTO as per your validation logic
        // Set fields to null or invalid values
        // For example: invalidOrderDTO.setTotalPrice(new BigDecimal("-1")); // Negative price to make it invalid

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // For LocalDateTime handling
        String orderJson = objectMapper.writeValueAsString(invalidOrderDTO);

        myMockMvc.perform(put("/orders/" + orderID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests retrieving an order with an invalid ID type (non-numeric) and expects a Bad Request response.
     */
    @Test
    public void whenGetOrderByInvalidIDType_thenBadRequest() throws Exception {
        myMockMvc.perform(get("/orders/abc")) // Non-numeric ID
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests deleting an order with an invalid ID type (non-numeric) and expects a Bad Request response.
     */
    @Test
    public void whenDeleteOrderByInvalidIDType_thenBadRequest() throws Exception {
        myMockMvc.perform(delete("/orders/abc")) // Non-numeric ID
                .andExpect(status().isBadRequest());
    }




}
