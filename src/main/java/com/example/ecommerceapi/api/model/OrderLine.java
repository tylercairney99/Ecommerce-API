package com.example.ecommerceapi.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Represents an order line item in the e-commerce system.
 * Maps to the 'order_lines' table in the database, holding details of each item in an order.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
@Entity // Marks this class as a JPA entity (i.e., a persistent domain object).
@Table(name = "order_lines") // Specifies the table in the database with which this entity is associated.
public class OrderLine {

    @Id // Marks this field as the primary key of the entity.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configures the way of increment of the specified column(field).

    /**
     * OrderLine ID.
     */
    private Long myOrderLineID;

    /**
     * Order Object.
     */
    @ManyToOne // Indicates a many-to-one relationship between 'OrderLine' and 'Order'.
    @JoinColumn(name = "order_id", referencedColumnName = "order_id") // Foreign key relationship
    private Order myOrder;

    /**
     * Product Object.
     */
    @ManyToOne // Indicates a many-to-one relationship between 'OrderLine' and 'Product'.
    @JoinColumn(name = "product_id", referencedColumnName = "product_id") // Foreign key relationship
    private Product myProduct;

    /**
     * Product quantity in order.
     */
    private int myQuantity;

    /**
     * Price of products in order.
     */
    @Column(precision = 10, scale = 2) // Optional!!! specify precision and scale if needed (just an example)
    private BigDecimal myPrice;

    /**
     * The total price for this order line item.
     */
    private BigDecimal myLineTotal;


    /**
     * Default constructor for JPA.
     * Required for JPA entity initialization.
     */
    public OrderLine() {
        // no arg constructor for JPA
    }

    /**
     * Constructs a new OrderLine with the specified order, product, quantity, and price.
     *
     * @param theOrder The associated order.
     * @param theProduct The associated product.
     * @param theQuantity The quantity of the product.
     * @param thePrice The price of the product.
     */
    public OrderLine(final Order theOrder, final Product theProduct, final int theQuantity, final BigDecimal thePrice) {
        this.myOrder = theOrder;
        this.myProduct = theProduct;
        this.myQuantity = theQuantity;
        this.myPrice = thePrice;
        this.myLineTotal = thePrice.multiply(BigDecimal.valueOf(theQuantity));
    }

    /**
     * Gets the unique identifier for this order line.
     *
     * @return the order line ID.
     */
    public Long getOrderLineID() {
        return myOrderLineID;
    }

    /**
     * Sets the unique identifier for this order line.
     *
     * @param theOrderLineID the new ID to be set for the order line.
     */
    public void setOrderLineID(final Long theOrderLineID) {
        this.myOrderLineID = theOrderLineID;
    }

    /**
     * Gets the order associated with this order line.
     *
     * @return the associated Order object.
     */
    public Order getOrder() {
        return myOrder;
    }

    /**
     * Sets the order associated with this order line.
     *
     * @param theOrder the Order object to associate with this order line.
     */
    public void setOrder(final Order theOrder) {
        this.myOrder = theOrder;
    }

    /**
     * Gets the product associated with this order line.
     *
     * @return the associated Product object.
     */
    public Product getProduct() {
        return myProduct;
    }

    /**
     * Sets the product associated with this order line.
     *
     * @param theProduct the Product object to associate with this order line.
     */
    public void setProduct(final Product theProduct) {
        this.myProduct = theProduct;
    }

    /**
     * Gets the quantity of the product in this order line.
     *
     * @return the quantity of the product.
     */
    public int getQuantity() {
        return myQuantity;
    }

    /**
     * Sets the quantity of the product in this order line.
     *
     * @param theQuantity the new quantity of the product.
     */
    public void setQuantity(final int theQuantity) {
        this.myQuantity = theQuantity;
    }

    /**
     * Gets the price of the product in this order line.
     *
     * @return the price of the product.
     */
    public BigDecimal getPrice() {
        return myPrice;
    }

    /**
     * Sets the price of the product in this order line.
     *
     * @param thePrice the new price of the product.
     */
    public void setPrice(final BigDecimal thePrice) {
        this.myPrice = thePrice;
    }

    // TODO ADDED

    public BigDecimal getLineTotal() {
        return myLineTotal;
    }

    public void setLineTotal(final BigDecimal theLineTotal) {
        this.myLineTotal = theLineTotal;
    }
}
