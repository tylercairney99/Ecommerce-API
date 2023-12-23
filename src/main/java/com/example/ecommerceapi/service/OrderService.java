package com.example.ecommerceapi.service;

import com.example.ecommerceapi.api.model.Order;
import com.example.ecommerceapi.api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service class for handling order operations.
 * This class provides CRUD (Create, Read, Update, Delete) operations for orders.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
@Service
public class OrderService {

    /**
     * The repository for order data.
     */
    private final OrderRepository myOrderRepository;

    /**
     * Constructs a OrderService with the provided order repository.
     *
     *
     * @param theOrderRepository The order repository to be used by this service.
     */
    @Autowired
    public OrderService(final OrderRepository theOrderRepository) {
        this.myOrderRepository = theOrderRepository;
    }

    /**
     * Adds a new order to the repository.
     *
     * @param theOrder (The order to be added)
     * @return The added order
     */
    public Order addOrder(final Order theOrder) { // Create
        if (theOrder == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        return myOrderRepository.save(theOrder);
    }

    /**
     * Retrieves all orders from the repository.
     *
     * @return A list of all orders
     */
    public List<Order> getAllOrders() { // Read
        return myOrderRepository.findAll();
    }

    /**
     * Retrieves a order based on its unique ID.
     *
     * @param theOrderID (Order ID)
     * @return An Optional containing the order if found, or empty otherwise
     */
    public Optional<Order> getOrderByID(final Long theOrderID) { // Read
        return myOrderRepository.findById(theOrderID);
    }

    /**
     * Updates the details of an existing order.
     *
     * @param theOrderID (The ID of the order to update)
     * @param theOrderDetails (The order details to be updated)
     * @return The updated order.
     * @throws ResponseStatusException If the order with the given ID cannot be found.
     */
    public Order updateOrder(final Long theOrderID, final Order theOrderDetails) { // Update
        if (theOrderDetails == null) {
            throw new IllegalArgumentException("Order details cannot be null");
        }
        if (theOrderDetails.getOrderDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Order date cannot be in the future");
        }
        if (theOrderDetails.getTotalPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total price cannot be negative");
        }

        final Order order = myOrderRepository.findById(theOrderID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with ID " + theOrderID + " not found"));

        if (theOrderDetails.getOrderDate() != null) {
            order.setOrderDate(theOrderDetails.getOrderDate());
        }
        if (theOrderDetails.getStatus() != null) {
            order.setStatus(theOrderDetails.getStatus());
        }
        if (theOrderDetails.getTotalPrice() != null) {
            order.setTotalPrice(theOrderDetails.getTotalPrice());
        }
        if (theOrderDetails.getMyUser() != null) {
            order.setUser(theOrderDetails.getMyUser());
        }

        // can add more logic to change orders here

        return myOrderRepository.save(order);
    }

    /**
     * Deletes an order from the repository by its ID.
     *
     * @param theOrderID The ID of the order to be deleted.
     * @throws ResponseStatusException If the order with the given ID cannot be found.
     */
    public void deleteOrder(final Long theOrderID) { // Delete
        final Order order = myOrderRepository.findById(theOrderID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with ID " + theOrderID + " not found"));

        myOrderRepository.delete(order);
    }
}



