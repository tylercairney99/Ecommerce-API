package com.example.ecommerceapi.service;

import com.example.ecommerceapi.api.model.OrderLine;
import com.example.ecommerceapi.api.repository.OrderLineRepository;
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
public class OrderLineService {

    /**
     * The repository for order line data.
     */
    private final OrderLineRepository myOrderLineRepository;

    /**
     * Constructs a OrderLineService with the provided product repository.
     *
     *
     * @param theOrderLineRepository The product repository to be used by this service.
     */
    @Autowired
    public OrderLineService(final OrderLineRepository theOrderLineRepository) {
        this.myOrderLineRepository = theOrderLineRepository;
    }

    /**
     * Adds a new order line to the repository.
     *
     * @param theOrderLine (The order line to be added)
     * @return The added order line
     */
    public OrderLine addOrderLine(final OrderLine theOrderLine) { // Create
        return myOrderLineRepository.save(theOrderLine);
    }

    /**
     * Retrieves all order lines from the repository.
     *
     * @return A list of all order lines
     */
    public List<OrderLine> getAllOrderLines() { // Read
        return myOrderLineRepository.findAll();
    }

    /**
     * Retrieves a order line based on its unique ID.
     *
     * @param theOrderLineID (Order ID)
     * @return An Optional containing the order if found, or empty otherwise
     */
    public Optional<OrderLine> getOrderLineByID(final Long theOrderLineID) { // Read
        return myOrderLineRepository.findById(theOrderLineID);
    }

    /**
     * Updates the details of an existing order line.
     * If the order line with the specified ID is not found, a ResponseStatusException is thrown.
     *
     * @param theOrderLineID (The unique identifier of the order line to update)
     * @param theOrderLineDetails (The details of the order line to be updated)
     * @return The updated order line
     * @throws ResponseStatusException If the order line with the given ID cannot be found
     */
    public OrderLine updateOrderLine(final Long theOrderLineID, final OrderLine theOrderLineDetails) { // Update
        final OrderLine orderLine = myOrderLineRepository.findById(theOrderLineID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderLine with ID " + theOrderLineID + " not found"));

        if (theOrderLineDetails.getOrder() != null) {
            orderLine.setOrder(theOrderLineDetails.getOrder());
        }
        if (theOrderLineDetails.getProduct() != null) {
            orderLine.setProduct(theOrderLineDetails.getProduct());
        }
        orderLine.setQuantity(theOrderLineDetails.getQuantity()); // not checking for null because quantity is a primitive (int)

        if (theOrderLineDetails.getPrice() != null) {
            orderLine.setPrice(theOrderLineDetails.getPrice());
        }

        // can add more logic to change order lines here

        return myOrderLineRepository.save(orderLine);
    }

    /**
     * Deletes an order line from the repository by its ID.
     *
     * @param theOrderLineID The ID of the order line to be deleted.
     * @throws ResponseStatusException If the order line with the given ID cannot be found.
     */
    public void deleteOrderLine(final Long theOrderLineID) {
        final OrderLine orderLine = myOrderLineRepository.findById(theOrderLineID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderLine with ID " + theOrderLineID + " not found"));

        myOrderLineRepository.delete(orderLine);
    }
}
