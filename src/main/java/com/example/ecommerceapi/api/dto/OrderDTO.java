package com.example.ecommerceapi.api.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;
import com.example.ecommerceapi.api.model.OrderLine;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import java.util.ArrayList;

/**
 * Data Transfer Object (DTO) for order data in the e-commerce application.
 *
 * This class is used to transfer order data between different layers of the application,
 * particularly between the persistence layer and the client-facing controller. It encapsulates
 * details about an order, including customer information, order lines, total amount, and date of order.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
public class OrderDTO {

    /**
     * The unique identifier of the order.
     */
    private Long myOrderID;

    /**
     * The unique identifier of the user who placed the order.
     */
    @NotNull(message = "User ID is required")
    private Long myUserID;

    /**
     * The list of order line items.
     */
    @NotNull(message = "Order lines cannot be null")
    private List<OrderLineDTO> myOrderlines = new ArrayList<>();

    /**
     * The date and time when the order was placed.
     */
    @NotNull(message = "Order date is required")
    @PastOrPresent(message = "Order date must be in the past or present")
    private LocalDateTime myOrderDate;

    /**
     * The total amount of the order.
     */
    @NotNull(message = "Total price cannot be null")
    @Min(value = 0, message = "Total price must be a positive value")
    private BigDecimal myTotalAmount;

    /**
     * Default no-argument constructor.
     */
    public OrderDTO() {
        // no arg constructor
    }

    /**
     * Constructs a new OrderDTO with the specified parameters.
     *
     * @param theOrderID       The unique identifier of the order.
     * @param theUserID        The unique identifier of the user who placed the order.
     * @param theOrderLines    The list of order line items.
     * @param theOrderDate     The date and time when the order was placed.
     * @param theTotalAmount   The total amount of the order.
     */
    public OrderDTO(final Long theOrderID, final Long theUserID, final List<OrderLineDTO> theOrderLines,
                    final LocalDateTime theOrderDate, final BigDecimal theTotalAmount) {

        this.myOrderID = theOrderID;
        this.myUserID = theUserID;
        this.myOrderlines = theOrderLines;
        this.myOrderDate = theOrderDate;
        this.myTotalAmount = theTotalAmount;
    }

    /**
     * Gets the unique identifier of the order.
     *
     * @return the unique identifier of the order.
     */
    public Long getOrderID() {
        return myOrderID;
    }

    /**
     * Sets the unique identifier of the order.
     *
     * @param theOrderID the new unique identifier for the order.
     */
    public void setOrderID(final Long theOrderID) {
        this.myOrderID = theOrderID;
    }

    /**
     * Gets the unique identifier of the user who placed the order.
     *
     * @return The unique identifier of the user.
     */
    public Long getUserID() {
        return myUserID;
    }

    /**
     * Sets the unique identifier of the user who placed the order.
     *
     * @param theUserID The new unique identifier for the user.
     */
    public void setUserID(final Long theUserID) {
        this.myUserID = theUserID;
    }

    /**
     * Gets the list of order line items.
     *
     * @return The list of order line items.
     */
    public List<OrderLineDTO> getOrderLines() {
        return myOrderlines;
    }

    /**
     * Sets the list of order line items.
     *
     * @param theOrderLines The new list of order line items.
     */
    public void setOrderLines(final List<OrderLineDTO> theOrderLines) {
        this.myOrderlines = theOrderLines;
    }

    /**
     * Gets the date and time when the order was placed.
     *
     * @return The date and time of the order.
     */
    public LocalDateTime getOrderDate() {
        return myOrderDate;
    }

    /**
     * Sets the date and time when the order was placed.
     *
     * @param theOrderDate The new date and time for the order.
     */
    public void setOrderDate(final LocalDateTime theOrderDate) {
        this.myOrderDate = theOrderDate;
    }

    /**
     * Gets the total amount of the order.
     *
     * @return The total amount of the order.
     */
    public BigDecimal getTotalAmount() {
        return myTotalAmount;
    }

    /**
     * Sets the total amount of the order.
     *
     * @param theTotalAmount The new total amount for the order.
     */
    public void setTotalAmount(final BigDecimal theTotalAmount) {
        this.myTotalAmount = theTotalAmount;
    }

    // Helper Method for adding an order line to an order

    /**
     * Adds an order line item to the order.
     *
     * @param theOrderLine (The order line item to add)
     */
    public void addOrderLine(final OrderLineDTO theOrderLine) {
        this.myOrderlines.add(theOrderLine);
    }

}
