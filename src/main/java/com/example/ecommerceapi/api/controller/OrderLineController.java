package com.example.ecommerceapi.api.controller;

import com.example.ecommerceapi.api.model.OrderLine;
import com.example.ecommerceapi.service.OrderLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

/**
 * REST controller for managing order lines.
 *
 * This controller provides endpoints for CRUD (Create, Read, Update, Delete) operations on order line entities,
 * facilitating the management of individual items within orders in the e-commerce system. It interfaces with
 * the OrderLineService to execute business logic and handle data transactions.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
@RestController // Controller for order line operations at '/orderlines' endpoint
@RequestMapping("/orderlines")
public class OrderLineController {

    /**
     * Service for handling order line-related operations.
     */
    private final OrderLineService myOrderLineService;

    /**
     * Constructs an OrderLineController with dependency injection of the OrderLineService.
     *
     * @param theOrderLineService (The service handling order line-related business logic)
     */
    @Autowired // Automatically injects an instance of OrderLineService
    public OrderLineController(final OrderLineService theOrderLineService) {
        this.myOrderLineService = theOrderLineService;
    }

    /**
     * Retrieves all order lines.
     *
     * @return A list of all OrderLine objects.
     */
    @GetMapping // Endpoint for getting all order lines
    public List<OrderLine> getAllOrderLines() {
        return myOrderLineService.getAllOrderLines();
    }

    /**
     * Retrieves a specific order line by its unique identifier.
     *
     * @param theOrderLineID (The ID of the order line to retrieve)
     * @return The OrderLine object if found.
     * @throws ResponseStatusException If no order line with the given ID is found.
     */
    @GetMapping("/{theOrderLineID}") // Endpoint to get an order line by its ID
    public OrderLine gerOrderLineByID(@PathVariable final Long theOrderLineID) {
        return myOrderLineService.getOrderLineByID(theOrderLineID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderLine with ID " + theOrderLineID + " not found"));
    }

    /**
     * Creates a new order line.
     *
     * @param theOrderLine (The OrderLine object to be created)
     * @return The newly created OrderLine object.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Endpoint to create a new order line, returns HTTP 201 status on creation
    public OrderLine addOrderLine(@RequestBody final OrderLine theOrderLine) {
        return myOrderLineService.addOrderLine((theOrderLine));
    }

    /**
     * Updates an existing order line's details.
     *
     * @param theOrderLineID (The ID of the order line to update)
     * @param theOrderLineDetails (The OrderLine object containing updated details)
     * @return The updated OrderLine object.
     */
    @PutMapping("/{theOrderLineID}") // Endpoint to update an order line by its ID
    public OrderLine updateOrderLine(@PathVariable final Long theOrderLineID, @RequestBody final OrderLine theOrderLineDetails) {
        return myOrderLineService.updateOrderLine(theOrderLineID, theOrderLineDetails);
    }

    /**
     * Deletes an order line by its ID.
     *
     * @param theOrderLineID (The ID of the order line to be deleted)
     */
    @DeleteMapping("/{theOrderLineID}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Endpoint to delete an order line by its ID, returns HTTP 204 status on successful deletion
    public void deleteOrderLine(@PathVariable final Long theOrderLineID) {
        myOrderLineService.deleteOrderLine(theOrderLineID);
    }
}
