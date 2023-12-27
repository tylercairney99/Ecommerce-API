package com.example.ecommerceapi.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.ecommerceapi.api.controller.OrderLineController;
import com.example.ecommerceapi.api.dto.OrderDTO;
import com.example.ecommerceapi.api.dto.OrderLineDTO;
import com.example.ecommerceapi.api.dto.UserDTO;
import com.example.ecommerceapi.api.model.Order;
import com.example.ecommerceapi.api.model.Product;
import com.example.ecommerceapi.api.model.OrderLine;
import com.example.ecommerceapi.config.RestExceptionHandler;
import com.example.ecommerceapi.service.OrderLineService;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.server.ResponseStatusException;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.util.*;

/**
 * This class contains test methods for the OrderLineController class to test various CRUD operations.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
@ExtendWith(SpringExtension.class)
public class OrderLineControllerTest {

    /**
     * The MockMvc instance for performing HTTP request and response simulations.
     */
    private MockMvc myMockMvc;

    /**
     * A mock instance of the OrderLineService class used for testing purposes.
     */
    @Mock
    private OrderLineService myOrderLineService;

    /**
     * The OrderLineController instance with the injected mock OrderLineService.
     */
    @InjectMocks
    private OrderLineController myOrderLineController;

    /**
     * A sample OrderLineDTO used for test cases.
     */
    private OrderLineDTO myOrderLineDTO;

    /**
     * A sample OrderLine instance used for test cases.
     */
    private OrderLine myOrderLine;

    /**
     * The ModelMapper instance used for mapping OrderLine to OrderLineDTO and vice versa.
     */
    private ModelMapper myModelMapper;

    /**
     * Initializes the ModelMapper and OrderLineController, and sets up the MockMvc for testing.
     */
    @BeforeEach
    public void setUp() {
        myModelMapper = new ModelMapper();
        myOrderLineController = new OrderLineController(myOrderLineService, myModelMapper);
        myMockMvc = MockMvcBuilders.standaloneSetup(myOrderLineController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        // Create mock Order and Product objects
        Order mockOrder = new Order(); // Assuming Order has a no-arg constructor
        Product mockProduct = new Product(); // Assuming Product has a no-arg constructor

        // Sample values for quantity and price
        int sampleQuantity = 5;
        BigDecimal samplePrice = new BigDecimal("29.99");

        // Initialize OrderLine with these mock objects and sample values
        myOrderLine = new OrderLine(mockOrder, mockProduct, sampleQuantity, samplePrice);

        // Map to OrderLineDTO
        myOrderLineDTO = myModelMapper.map(myOrderLine, OrderLineDTO.class);
    }

    /**
     * Tests retrieving all order lines and expects a list of order lines in the response.
     */
    @Test
    public void whenGetAllOrderLines_thenReturnOrderLineList() throws Exception {
        // Given: Mock the OrderLineService to return a list containing a sample order line.
        given(myOrderLineService.getAllOrderLines()).willReturn(Collections.singletonList(myOrderLine));

        myMockMvc.perform(get("/orderlines"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].orderLineID", is(myOrderLineDTO.getOrderLineID())));
    }

    /**
     * Tests retrieving an order line by ID and expects an order line DTO in the response.
     */
    @Test
    public void whenGetOrderLineByID_thenReturnOrderLineDTO() throws Exception {
        final Long orderLineID = 1L;
        given(myOrderLineService.getOrderLineByID(orderLineID)).willReturn(Optional.of(myOrderLine));

        myMockMvc.perform(get("/orderlines/" + orderLineID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderLineID", is(myOrderLineDTO.getOrderLineID())));
    }

    /**
     * Tests adding an order line and expects the saved order line DTO in the response.
     */
    @Test
    public void whenAddOrderLine_thenReturnCreatedOrderLineDTO() throws Exception {
        // Initialize a valid OrderLineDTO
        OrderLineDTO validOrderLineDTO = new OrderLineDTO();
        validOrderLineDTO.setProductId(1L); // Valid Product ID
        validOrderLineDTO.setQuantity(3);   // Valid quantity
        validOrderLineDTO.setUnitPrice(new BigDecimal("50.00")); // Valid unit price

        // Mock the service response
        OrderLine mockOrderLine = new OrderLine();
        // Set an example ID for the mock order line, and other required fields
        mockOrderLine.setOrderLineID(1L);
        mockOrderLine.setQuantity(validOrderLineDTO.getQuantity());
        mockOrderLine.setPrice(validOrderLineDTO.getUnitPrice());
        // Additional mock settings if necessary

        given(myOrderLineService.addOrderLine(any(OrderLine.class))).willReturn(mockOrderLine);

        // Convert OrderLineDTO to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String orderLineJson = objectMapper.writeValueAsString(validOrderLineDTO);

        // Perform the test
        myMockMvc.perform(post("/orderlines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderLineJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderLineID", is(mockOrderLine.getOrderLineID().intValue())));
    }

    /**
     * Tests updating an order line and expects the updated order line DTO in the response.
     */
    @Test
    public void whenUpdateOrderLine_thenReturnUpdatedOrderLineDTO() throws Exception {
        final Long orderLineID = 1L;
        // Mock an OrderLine instance that represents the updated order line
        OrderLine updatedOrderLine = new OrderLine(); // Assuming OrderLine has a no-arg constructor
        updatedOrderLine.setOrderLineID(orderLineID);
        updatedOrderLine.setQuantity(10); // Example updated quantity
        updatedOrderLine.setPrice(new BigDecimal("20.00")); // Example updated price

        // Mock service method
        given(myOrderLineService.updateOrderLine(eq(orderLineID), ArgumentMatchers.any(OrderLine.class))).willReturn(updatedOrderLine);

        // Initialize a valid OrderLineDTO with updated values
        OrderLineDTO validOrderLineDTO = new OrderLineDTO();
        validOrderLineDTO.setProductId(2L); // Updated Product ID
        validOrderLineDTO.setQuantity(10);  // Updated Quantity
        validOrderLineDTO.setUnitPrice(new BigDecimal("20.00")); // Updated Unit Price

        ObjectMapper objectMapper = new ObjectMapper();
        String orderLineJson = objectMapper.writeValueAsString(validOrderLineDTO);

        // Perform the test
        myMockMvc.perform(put("/orderlines/" + orderLineID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderLineJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderLineID", is(orderLineID.intValue())));
    }

    /**
     * Tests deleting an order line and expects a No Content response.
     */
    @Test
    public void whenDeleteOrderLine_thenStatusNoContent() throws Exception {
        final Long orderLineID = 1L;
        willDoNothing().given(myOrderLineService).deleteOrderLine(orderLineID);

        myMockMvc.perform(delete("/orderlines/" + orderLineID))
                .andExpect(status().isNoContent());
    }

    /**
     * Tests retrieving an order line by a non-existent ID and expects a Not Found response.
     */
    @Test
    public void whenGetOrderLineByNonExistentID_thenThrowNotFoundException() throws Exception {
        final Long nonExistentOrderLineID = 999L;
        given(myOrderLineService.getOrderLineByID(nonExistentOrderLineID)).willReturn(Optional.empty());

        myMockMvc.perform(get("/orderlines/" + nonExistentOrderLineID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"OrderLine with ID " + nonExistentOrderLineID + " not found\"",
                        result.getResolvedException().getMessage()));
    }

    /**
     * Tests adding an order line with invalid data and expects a Bad Request response.
     */
    @Test
    public void whenAddOrderLineWithInvalidData_thenBadRequest() throws Exception {
        final OrderLineDTO invalidOrderLineDTO = new OrderLineDTO();
        final String orderLineJson = new ObjectMapper().writeValueAsString(invalidOrderLineDTO);

        myMockMvc.perform(post("/orderlines/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderLineJson))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests order an order line with invalid data and expects a Bad Request response.
     */
    @Test
    public void whenUpdateOrderLineWithInvalidData_thenBadRequest() throws Exception {
        final Long orderLineID = 1L;
        OrderLineDTO invalidOrderLineDTO = new OrderLineDTO(); // Create an invalid OrderLineDTO as per your validation logic
        // Set fields to null or invalid values

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // For LocalDateTime handling
        String orderLineJson = objectMapper.writeValueAsString(invalidOrderLineDTO);

        myMockMvc.perform(put("/orderlines/" + orderLineID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderLineJson))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests retrieving an order line with an invalid ID type (non-numeric) and expects a Bad Request response.
     */
    @Test
    public void whenGetOrderLineByInvalidIDType_thenBadRequest() throws Exception {
        myMockMvc.perform(get("/orderlines/abc")) // Non-numeric ID
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests deleting an order line with an invalid ID type (non-numeric) and expects a Bad Request response.
     */
    @Test
    public void whenDeleteOrderLineByInvalidIDType_thenBadRequest() throws Exception {
        myMockMvc.perform(delete("/orderlines/abc")) // Non-numeric ID
                .andExpect(status().isBadRequest());
    }
}
