package com.example.ecommerceapi.api.controller;

import com.example.ecommerceapi.service.OrderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.ecommerceapi.api.model.Order;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.*;

/**
 * REST controller for managing orders.
 *
 * This controller provides endpoints for CRUD (Create, Read, Update, Delete) operations on order entities,
 * allowing for the management of order data within the e-commerce system. It interacts with the OrderService
 * to perform business logic and data persistence.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
@RestController // Controller for order operations at '/orders' endpoint
@RequestMapping("/orders")
public class OrderController {

    /**
     * Service for handling order-related operations.
     */
    private final OrderService myOrderService;

    /**
     * Constructs a OrderController with dependency injection of the OrderService.
     *
     * @param theOrderService (The service handling order-related business logic)
     */
    @Autowired // Automatically injects an instance of OrderService
    public OrderController(final OrderService theOrderService) {
        this.myOrderService = theOrderService;
    }

    /**
     * Retrieves a list of all orders.
     *
     * @return A list of Order objects.
     */
    @GetMapping // Endpoint for getting all orders
    public List<Order> getAllOrders() {
        return myOrderService.getAllOrders();
    }

    /**
     * Retrieves a specific order by its unique identifier.
     *
     * @param theOrderID (The ID of the order to retrieve)
     * @return The Order object if found.
     * @throws ResponseStatusException If no order with the given ID is found.
     */
    @GetMapping("/{theOrderID}") // Endpoint to get an order by its ID
    public Order getOrderByID(@PathVariable final Long theOrderID) {
        return myOrderService.getOrderByID(theOrderID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with ID " + theOrderID + " not found"));
    }

    /**
     * Creates a new order.
     *
     * @param theOrder (The Order object to be created)
     * @return The newly created Order object.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Endpoint to create a new order, returns HTTP 201 status on creation
    public Order addOrder(@RequestBody final Order theOrder) {
        return myOrderService.addOrder(theOrder);
    }

    /**
     * Updates an existing order's information.
     *
     * @param theOrderID (The ID of the order to update)
     * @param theOrderDetails The Order object containing updated information.
     * @return The updated Product object.
     */
    @PutMapping("/{theOrderID}") // Endpoint to update an order by its ID
    public Order updateOrder(@PathVariable final Long theOrderID, @RequestBody final Order theOrderDetails) {
        return myOrderService.updateOrder(theOrderID, theOrderDetails);
    }

    /**
     * Deletes an order by its ID.
     *
     * @param theOrderID The ID of the order to be deleted.
     */
    @DeleteMapping("/{theOrderID}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Endpoint to delete an order by its ID, returns HTTP 204 status on successful deletion
    public void deleteOrder(@PathVariable final Long theOrderID) {
        myOrderService.deleteOrder(theOrderID);
    }



}
