package com.example.ecommerceapi.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an order entity in the e-commerce system.
 * Maps to the 'orders' table in the database, holding order details.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
@Entity // Marks this class as a JPA entity (i.e., a persistent domain object).
@Table(name = "orders") // Specifies the table in the database with which this entity is associated.
public class Order {

    @Id // Marks this field as the primary key of the entity.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configures the way of increment of the specified column(field).

    /**
     * Order ID.
     */
    @Column(name = "order_id")
    private Long myOrderID;

    /**
     * Order date.
     */
    private LocalDateTime myOrderDate;

    /**
     * Order status.
     */
    private String myOrderStatus;

    /**
     * Total price of order.
     */
    private BigDecimal myTotalPrice;

    /**
     * User Object.
     */
    @ManyToOne // Indicates a many-to-one relationship between 'Order' and 'User'.
    @JoinColumn(name = "user_id", referencedColumnName = "user_id") // Foreign key relationship
    @Getter // Lombok generates the getter getUser() method
    private User myUser;

    /**
     * Represents a one-to-many relationship between 'myOrder' and 'OrderLine'.
     * 'myOrderLines' is a collection of 'OrderLine' objects associated with 'myOrder'.
     * Cascade type 'ALL' means changes to 'myOrder' cascade to 'myOrderLines'.
     * Orphan removal is enabled, removing orphaned 'OrderLine' objects.
     */
    @OneToMany(mappedBy = "myOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLine> myOrderLines = new ArrayList<>();

    /**
     * Default constructor for JPA.
     * This is required for JPA entity initialization.
     */
    public Order() {
        // no arg constructor for JPA
    }

    /**
     * Constructs a new Order with the specified date, status, total price, and associated user.
     *
     * @param theOrderDate The date of the order.
     * @param theStatus The status of the order.
     * @param theTotalPrice The total price of the order.
     * @param theUser The user associated with this order.
     */
    public Order(final LocalDateTime theOrderDate, final String theStatus, final BigDecimal theTotalPrice, final User theUser) {
        this.myOrderDate = theOrderDate;
        this.myOrderStatus = theStatus;
        this.myTotalPrice = theTotalPrice;
        this.myUser = theUser;
    }

    /**
     * Gets the ID of the order.
     *
     * @return The order ID.
     */
    public Long getOrderID() {
        return myOrderID;
    }

    /**
     * Sets the ID of the order.
     *
     * @param theOrderID The new ID for the order.
     */
    public void setOrderID(final Long theOrderID) {
        this.myOrderID = theOrderID;
    }

    /**
     * Gets the date of the order.
     *
     * @return The order date.
     */
    public LocalDateTime getOrderDate() {
        return myOrderDate;
    }

    /**
     * Sets the date of the order.
     *
     * @param theOrderDate The new date for the order.
     */
    public void setOrderDate(final LocalDateTime theOrderDate) {
        this.myOrderDate = theOrderDate;
    }

    /**
     * Gets the status of the order.
     *
     * @return The order status.
     */
    public String getStatus() {
        return myOrderStatus;
    }

    /**
     * Sets the status of the order.
     *
     * @param theStatus The new status for the order.
     */
    public void setStatus(final String theStatus) {
        this.myOrderStatus = theStatus;
    }

    /**
     * Gets the total price of the order.
     *
     * @return The total price of the order.
     */
    public BigDecimal getTotalPrice() {
        return myTotalPrice;
    }

    /**
     * Sets the total price of the order.
     *
     * @param theTotalPrice The new total price for the order.
     */
    public void setTotalPrice(final BigDecimal theTotalPrice) {
        this.myTotalPrice = theTotalPrice;
    }

    /**
     * Sets the User associated with this order.
     *
     * @param theUser The User to be associated with this order.
     */
    public void setUser(final User theUser) {
        this.myUser = theUser;
    }

    /**
     * Sets the list of order lines associated with this order.
     *
     * @param orderLines The list of order lines to set for this order.
     */
    public void setOrderLines(List<OrderLine> orderLines) {
        this.myOrderLines = orderLines;
    }

    /**
     * Adds an order line to this order and sets the relationship between them.
     *
     * @param orderLine The order line to add to this order.
     */
    public void addOrderLine(OrderLine orderLine) {
        orderLine.setOrder(this); // Set the relationship
        this.myOrderLines.add(orderLine);
    }

    /**
     * Retrieves the list of order lines associated with this order.
     *
     * @return The list of order lines associated with this order.
     */
    public List<OrderLine> getOrderLines() {
        return myOrderLines;
    }


}
