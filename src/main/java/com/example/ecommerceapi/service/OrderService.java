package com.example.ecommerceapi.service;

import com.example.ecommerceapi.api.model.Order;
import com.example.ecommerceapi.api.repository.OrderRepository;
import com.example.ecommerceapi.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

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
     * Constructs a ProductService with the provided product repository.
     *
     *
     * @param theOrderRepository The product repository to be used by this service.
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
    public Order addOrder(final Order theOrder) {
        return myOrderRepository.save(theOrder);
    }

    /**
     * Retrieves all orders from the repository.
     *
     * @return A list of all orders
     */
    public List<Order> getAllOrders() {
        return myOrderRepository.findAll();
    }

    public Optional<Order> getOrderByID(final Long theOrderID) {
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
    public Order updateOrder(final Long theOrderID, final Order theOrderDetails) {
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

        return myOrderRepository.save(order);
    }

    /**
     * Deletes an order from the repository by its ID.
     *
     * @param theOrderID The ID of the order to be deleted.
     * @throws ResponseStatusException If the order with the given ID cannot be found.
     */
    public void deleteOrder(final Long theOrderID) {
        final Order order = myOrderRepository.findById(theOrderID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with ID " + theOrderID + " not found"));

        myOrderRepository.delete(order);
    }
}



