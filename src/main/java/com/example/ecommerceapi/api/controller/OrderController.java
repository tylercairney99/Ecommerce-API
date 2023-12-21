package com.example.ecommerceapi.api.controller;

import com.example.ecommerceapi.api.dto.OrderDTO;
import com.example.ecommerceapi.api.model.Order;
import com.example.ecommerceapi.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

import java.util.*;
import java.util.stream.Collectors;

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
     * ModelMapper for converting between DTO and entity classes.
     */
    private final ModelMapper myModelMapper;

    /**
     * Constructs a OrderController with dependency injection of the OrderService.
     *
     * @param theOrderService (The service handling order-related business logic)
     * @param theModelMapper (Mapping between DTO and entity classes)
     */
    @Autowired // Automatically injects an instance of OrderService
    public OrderController(final OrderService theOrderService, final ModelMapper theModelMapper) {
        this.myOrderService = theOrderService;
        this.myModelMapper = theModelMapper;
    }

    /**
     * Retrieves all orders and returns them as a list of OrderDTOs.
     *
     * This method fetches a list of all Order entities, maps them to OrderDTOs,
     * and returns the list of DTOs. Useful for providing a view of user data
     * suitable for client-side use.
     *
     * @return A list of OrderDTOs representing all users.
     */
    @GetMapping // Endpoint for getting all orders
    public List<OrderDTO> getAllOrders() {
        final List<Order> orders = myOrderService.getAllOrders();
        return orders.stream()
                .map(order -> myModelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific order by its unique identifier and returns it as a OrderDTO.
     *
     * This method fetches a order from the database using their ID. If found, the order
     * is mapped to a OrderDTO and returned. If no order is found with the provided ID,
     * a ResponseStatusException with HttpStatus.NOT_FOUND is thrown.
     *
     * @param theOrderID (The unique identifier of the order to be retrieved)
     * @return A OrderDTO representing the retrieved user.
     * @throws ResponseStatusException If no order with the given ID is found.
     */
    @GetMapping("/{theOrderID}") // Endpoint to get an order by its ID
    public OrderDTO getOrderByID(@PathVariable final Long theOrderID) {
        final Order order = myOrderService.getOrderByID(theOrderID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with ID " + theOrderID + " not found"));
        return myModelMapper.map(order, OrderDTO.class);
    }

    /**
     * Creates and adds a new order to the system.
     *
     * Accepts a OrderDTO object, validates it, and maps it to a Order entity
     * for creation. Returns the created order as a UserDTO.
     *
     * @param theOrderDTO (The DTO containing the new order's data)
     * @return The created user as a OrderDTO.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Endpoint to create a new order, returns HTTP 201 status on creation
    public OrderDTO addOrder(@RequestBody final Order theOrderDTO) {
        final Order order = myModelMapper.map(theOrderDTO, Order.class);
        final Order createdOrder = myOrderService.addOrder(order);
        return myModelMapper.map(createdOrder, OrderDTO.class);
    }

    /**
     * Updates the information of an existing order.
     *
     * Accepts a OrderDTO object with updated data, validates it, and maps it
     * to the Order entity to update. The order to update is identified by OrderID.
     * Returns the updated order as a OrderDTO.
     *
     * @param theOrderID (The ID of the order to be updated)
     * @param theOrderDTO (The DTO containing the order's updated data)
     * @return The updated order as a OrderDTO.
     */
    @PutMapping("/{theOrderID}") // Endpoint to update an order by its ID
    public OrderDTO updateOrder(@PathVariable final Long theOrderID, @RequestBody final OrderDTO theOrderDTO) {
        final Order orderToUpdate = myModelMapper.map(theOrderDTO, Order.class);
        final Order updatedOrder = myOrderService.updateOrder(theOrderID, orderToUpdate);
        return myModelMapper.map(updatedOrder, OrderDTO.class);
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
