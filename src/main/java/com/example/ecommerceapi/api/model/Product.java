package com.example.ecommerceapi.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Represents a product entity in the e-commerce system.
 * This class maps to a database table and provides data fields for product attributes.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
@Entity // Marks this class as a JPA entity (i.e., a persistent domain object).
@Table(name = "products") // Specifies the table in the database with which this entity is associated.
public class Product {

    @Id  // Marks this field as the primary key of the entity.
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Configures the way of increment of the specified column(field).

    /**
     * Product ID.
     */
    private Long myID;

    /**
     * Product name.
     */
    private String myProductName;

    /**
     * Product price.
     */
    private BigDecimal myPrice;

    /**
     * Product quantity in stock.
     */
    private int myStockQuantity;

    /**
     * Default constructor for JPA.
     * This is required for JPA entity initialization.
     */
    public Product() {
        // no arg constructor for JPA
    }

    /**
     * Constructs a new Product with the specified details.
     *
     * @param theProductName The name of the product.
     * @param thePrice The price of the product.
     * @param theStockQuantity The quantity of the product in stock.
     */
    public Product(final String theProductName, final BigDecimal thePrice, final int theStockQuantity) {
        this.myProductName = theProductName;
        this.myPrice = thePrice;
        this.myStockQuantity = theStockQuantity;
    }

    /**
     * Gets the ID of the product.
     *
     * @return The product ID.
     */
    public Long getID() {
        return myID;
    }

    /**
     * Sets the ID of the product.
     *
     * @param theID The new ID for the product.
     */
    public void setID(final Long theID) {
        this.myID = theID;
    }

    /**
     * Gets the product name.
     *
     * @return The name of the product.
     */
    public String getProductName() {
        return myProductName;
    }

    /**
     * Sets the product name.
     *
     * @param theProductName The new name for the product.
     */
    public void setProductName(final String theProductName) {
        this.myProductName = theProductName;
    }

    /**
     * Gets the price of the product.
     *
     * @return The price of the product.
     */
    public BigDecimal getPrice() {
        return myPrice;
    }

    /**
     * Sets the price of the product.
     *
     * @param thePrice The new price for the product.
     */
    public void setPrice(final BigDecimal thePrice) {
        this.myPrice = thePrice;
    }

    /**
     * Gets the stock quantity of the product.
     *
     * @return The stock quantity of the product.
     */
    public int getStockQuantity() {
        return myStockQuantity;
    }

    /**
     * Sets the stock quantity of the product.
     *
     * @param theStockQuantity The new stock quantity for the product.
     */
    public void setStockQuantity(final int theStockQuantity) {
        this.myStockQuantity = theStockQuantity;
    }
}
