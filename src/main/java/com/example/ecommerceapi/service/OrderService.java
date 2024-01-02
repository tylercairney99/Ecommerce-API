package com.example.ecommerceapi.service;

import com.example.ecommerceapi.api.dto.OrderLineDTO;
import com.example.ecommerceapi.service.ProductService;
import com.example.ecommerceapi.api.model.Order;
import com.example.ecommerceapi.api.model.OrderLine;
import com.example.ecommerceapi.api.model.Product;
import com.example.ecommerceapi.api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    private final ProductService myProductService;

    /**
     * Constructs a OrderService with the provided order repository.
     *
     *
     * @param theOrderRepository The order repository to be used by this service.
     */
    @Autowired
    public OrderService(final OrderRepository theOrderRepository, final ProductService theProductService) {
        this.myOrderRepository = theOrderRepository;
        this.myProductService = theProductService;
    }

    // TODO ADDED THIS
    /**
     * Adds a new order to the repository.
     *
     * @param theOrder (The order to be added)
     * @return The added order
     * @throws IllegalArgumentException If order is null.
     */
    public Order addOrder(final Order theOrder) {
        if (theOrder == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderLine line : theOrder.getOrderLines()) {
            // Validate that price and quantity are not null
            if (line.getPrice() == null || line.getQuantity() == 0) {
                throw new IllegalArgumentException("OrderLine must have a valid price and quantity");
            }

            // Calculate the total for this line and add it to the order's total amount
            BigDecimal lineTotal = line.getPrice().multiply(BigDecimal.valueOf(line.getQuantity()));
            totalAmount = totalAmount.add(lineTotal);

            // Set lineTotal in OrderLine (if you have a setter for this in OrderLine)
            line.setLineTotal(lineTotal);

            // Set back-reference from OrderLine to Order
            line.setOrder(theOrder);
        }

        // Set the total price of the order
        theOrder.setTotalPrice(totalAmount);

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
     * @throws IllegalArgumentException If the order is null, date is in the future, or price is negative.
     * @throws ResponseStatusException If the order with the given ID cannot be found.
     */
    public Order updateOrder(final Long theOrderID, final Order theOrderDetails) {
        if (theOrderDetails == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order details cannot be null");
        }

        final Order existingOrder = myOrderRepository.findById(theOrderID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with ID " + theOrderID + " not found"));

        // Check for invalid order data
        if (theOrderDetails.getOrderDate() != null && theOrderDetails.getOrderDate().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order date cannot be in the future");
        }
        if (theOrderDetails.getTotalPrice() != null && theOrderDetails.getTotalPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Total price cannot be negative");
        }

        // Update order fields
        existingOrder.setOrderDate(theOrderDetails.getOrderDate() != null ? theOrderDetails.getOrderDate() : existingOrder.getOrderDate());
        existingOrder.setStatus(theOrderDetails.getStatus() != null ? theOrderDetails.getStatus() : existingOrder.getStatus());
        existingOrder.setUser(theOrderDetails.getMyUser() != null ? theOrderDetails.getMyUser() : existingOrder.getMyUser());

        // Clear and update order lines
        existingOrder.getOrderLines().clear();
        List<OrderLine> updatedOrderLines = theOrderDetails.getOrderLines().stream()
                .map(orderLineDTO -> {
                    Product product = myProductService.getProductByID(orderLineDTO.getProduct().getID())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found with ID: " + orderLineDTO.getProduct().getID()));

                    OrderLine newOrderLine = new OrderLine(existingOrder, product, orderLineDTO.getQuantity(), orderLineDTO.getPrice());
                    BigDecimal lineTotal = orderLineDTO.getPrice().multiply(BigDecimal.valueOf(orderLineDTO.getQuantity()));
                    newOrderLine.setLineTotal(lineTotal);
                    return newOrderLine;
                })
                .collect(Collectors.toList());
        existingOrder.setOrderLines(updatedOrderLines);

        // Recalculate total price
        BigDecimal newTotalPrice = updatedOrderLines.stream()
                .map(OrderLine::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        existingOrder.setTotalPrice(newTotalPrice);

        return myOrderRepository.save(existingOrder);
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



