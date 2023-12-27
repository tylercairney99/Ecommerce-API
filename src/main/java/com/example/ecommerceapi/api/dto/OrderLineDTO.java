package com.example.ecommerceapi.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for order line data in the e-commerce application.
 *
 * This class is used to transfer order line data between different layers of the application,
 * particularly between the persistence layer and the client-facing controller. It encapsulates
 * details about an individual order line item, such as the product, quantity, and line total.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
public class OrderLineDTO {

    /**
     * The unique identifier of the order line.
     */
    private Long myOrderLineID;

    /**
     * The unique identifier of the product in the order line.
     */
    @NotNull(message = "Product ID is required")
    private Long myProductID;

    /**
     * The quantity of the product in the order line.
     */
    @Min(value = 1, message = "Quantity must be at least 1")
    private int myQuantity;

    /**
     * The price of the product per unit.
     */
    @NotNull(message = "Unit price is required")
    private BigDecimal myUnitPrice;

    /**
     * The total price for this order line item.
     */
    private BigDecimal myLineTotal;

    // Default constructor
    public OrderLineDTO() {
    }

    // All-arguments constructor
    public OrderLineDTO(final Long theProductId, final int theQuantity, final BigDecimal theUnitPrice) {
        this.myProductID = theProductId;
        this.myQuantity = theQuantity;
        this.myUnitPrice = theUnitPrice;
        this.myLineTotal = theUnitPrice.multiply(new BigDecimal(theQuantity));
    }

    // Getters and Setters with JavaDoc comments

    /**
     * Gets the order line ID.
     *
     * @return the order line ID
     */
    public Long getOrderLineID() {
        return myOrderLineID;
    }

    /**
     * Sets the order line ID.
     *
     * @param theOrderLineID the new order line ID
     */
    public void setOrderLineID(final Long theOrderLineID) {
        this.myOrderLineID = theOrderLineID;
    }

    /**
     * Gets the product ID of the order line.
     *
     * @return the product ID.
     */
    public Long getProductId() {
        return myProductID;
    }

    /**
     * Sets the product ID of the order line.
     *
     * @param theProductId the new product ID.
     */
    public void setProductId(final Long theProductId) {
        this.myProductID = theProductId;
    }

    /**
     * Gets the quantity of the product in the order line.
     *
     * @return the quantity.
     */
    public int getQuantity() {
        return myQuantity;
    }

    /**
     * Sets the quantity of the product in the order line.
     *
     * @param theQuantity the new quantity.
     */
    public void setQuantity(final int theQuantity) {
        this.myQuantity = theQuantity;
        // Recalculate line total if quantity is set after initialization
        if (this.myUnitPrice != null) {
            this.myLineTotal = this.myUnitPrice.multiply(new BigDecimal(theQuantity));
        }
    }

    /**
     * Gets the unit price of the product in the order line.
     *
     * @return the unit price.
     */
    public BigDecimal getUnitPrice() {
        return myUnitPrice;
    }

    /**
     * Sets the unit price of the product in the order line.
     *
     * @param theUnitPrice the new unit price.
     */
    public void setUnitPrice(final BigDecimal theUnitPrice) {
        this.myUnitPrice = theUnitPrice;
        // Recalculate line total if unit price is set after initialization
        this.myLineTotal = theUnitPrice.multiply(new BigDecimal(this.myQuantity));
    }

    /**
     * Gets the total price for this order line item.
     *
     * @return the line total.
     */
    public BigDecimal getLineTotal() {
        return myLineTotal;
    }

    // Note: Typically, there's no setter for lineTotal as it's derived from quantity and unit price

}
