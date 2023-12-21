package com.example.ecommerceapi.api.controller;

import com.example.ecommerceapi.api.dto.OrderDTO;
import com.example.ecommerceapi.api.dto.OrderLineDTO;
import com.example.ecommerceapi.api.model.OrderLine;
import com.example.ecommerceapi.service.OrderLineService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

import java.util.*;
import java.util.stream.Collectors;

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
     * ModelMapper for converting between DTO and entity classes.
     */
    private final ModelMapper myModelMapper;

    /**
     * Constructs an OrderLineController with dependency injection of the OrderLineService.
     *
     * @param theOrderLineService (The service handling order line-related business logic)
     * @param theModelMapper (Mapping between DTO and entity classes)
     */
    @Autowired // Automatically injects an instance of OrderLineService
    public OrderLineController(final OrderLineService theOrderLineService, final ModelMapper theModelMapper) {
        this.myOrderLineService = theOrderLineService;
        this.myModelMapper = theModelMapper;
    }

    /**
     * Retrieves all order lines and returns them as a list of OrderLineDTOs.
     *
     * This method fetches a list of all order lines, maps them to OrderLineDTOs,
     * and returns the list of DTOs. Useful for providing a view of order line data
     * suitable for client-side use.
     *
     * @return A list of OrderLineDTOs representing all order lines.
     */
    @GetMapping // Endpoint for getting all order lines
    public List<OrderLineDTO> getAllOrderLines() {
        final List<OrderLine> orderLines = myOrderLineService.getAllOrderLines();
        return orderLines.stream()
                .map(orderLine -> myModelMapper.map(orderLine, OrderLineDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific order line by its unique identifier and returns it as an OrderLineDTO.
     *
     * This method fetches an order line from the database using its ID. If found, the order line
     * is mapped to an OrderLineDTO and returned. If no order line is found with the provided ID,
     * a ResponseStatusException with HttpStatus.NOT_FOUND is thrown.
     *
     * @param theOrderLineID (The unique identifier of the order line to be retrieved)
     * @return An OrderLineDTO representing the retrieved order line.
     * @throws ResponseStatusException If no order line with the given ID is found.
     */
    @GetMapping("/{theOrderLineID}") // Endpoint to get an order line by its ID
    public OrderLineDTO gerOrderLineByID(@PathVariable final Long theOrderLineID) {
        final OrderLine orderLine = myOrderLineService.getOrderLineByID(theOrderLineID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderLine with ID " + theOrderLineID + " not found"));
        return myModelMapper.map(orderLine, OrderLineDTO.class);
    }

    /**
     * Creates a new order line and returns it as an OrderLineDTO.
     *
     * This method takes an OrderDTO as input, maps it to an OrderLine entity,
     * and adds the new order line to the database. The newly created order line
     * is then mapped to an OrderLineDTO and returned. It returns an HTTP 201 status
     * upon successful creation.
     *
     * @param theOrderLineDTO (The OrderDTO representing the new order line to be created)
     * @return An OrderLineDTO representing the newly created order line.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Endpoint to create a new order line, returns HTTP 201 status on creation
    public OrderLineDTO addOrderLine(@Valid @RequestBody final OrderDTO theOrderLineDTO) {
        final OrderLine orderLine = myModelMapper.map(theOrderLineDTO, OrderLine.class);
        final OrderLine createdOrderLine = myOrderLineService.addOrderLine(orderLine);
        return myModelMapper.map(createdOrderLine, OrderLineDTO.class);
    }

    /**
     * Updates an order line by its unique identifier and returns the updated OrderLineDTO.
     *
     * This method takes the unique identifier of the order line to be updated and an updated
     * OrderLineDTO as input. It maps the updated DTO to an OrderLine entity and performs
     * the update operation in the database. The method returns the updated order line as an
     * OrderLineDTO.
     *
     * @param theOrderLineID (The unique identifier of the order line to be updated)
     * @param theOrderLineDTO (The updated OrderLineDTO containing the new data)
     * @return An OrderLineDTO representing the updated order line.
     */
    @PutMapping("/{theOrderLineID}") // Endpoint to update an order line by its ID
    public OrderLineDTO updateOrderLine(@PathVariable final Long theOrderLineID, @Valid @RequestBody final OrderLineDTO theOrderLineDTO) {
        final OrderLine orderLineToUpdate = myModelMapper.map(theOrderLineDTO, OrderLine.class);
        final OrderLine updatedOrderLine = myOrderLineService.updateOrderLine(theOrderLineID, orderLineToUpdate);
        return myModelMapper.map(updatedOrderLine, OrderLineDTO.class);
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
