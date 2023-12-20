package com.example.ecommerceapi.api.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.ecommerceapi.service.ProductService;
import com.example.ecommerceapi.api.model.Product;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.*;

/**
 * REST controller for managing products.
 *
 * This controller handles HTTP requests related to product operations, providing
 * an interface for CRUD (Create, Read, Update, Delete) operations on product entities.
 * It interacts with the ProductService to perform business logic and data manipulation.
 * The controller routes various HTTP requests to the appropriate service methods and
 * returns the corresponding responses to the client.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
@RestController // Controller for product operations at '/products' endpoint
@RequestMapping("/products")
public class ProductController {

    /**
     * Service for handling product-related operations.
     */
    private final ProductService myProductService;

    /**
     * Constructs a ProductController with dependency injection of the ProductService.
     *
     * @param theProductService (The service handling product-related business logic)
     */
    @Autowired // Automatically injects an instance of ProductService
    public ProductController(final ProductService theProductService) {
        this.myProductService = theProductService;
    }

    /**
     * Retrieves a list of all products.
     *
     * @return A list of Product objects.
     */
    @GetMapping // Endpoint for getting all products
    public List<Product> getAllProducts() {
        return myProductService.getAllProducts();
    }

    /**
     * Retrieves a specific product by its unique identifier.
     *
     * @param theProductID (The ID of the product to retrieve)
     * @return The Product object if found
     * @throws ResponseStatusException If no product with the given ID is found
     */
    @GetMapping("/{theProductID}") // Endpoint to get a product by its ID
    public Product getProductByID(@PathVariable final Long theProductID) {
        return myProductService.getProductByID(theProductID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with ID " + theProductID + " not found"));
    }

    /**
     * Creates a new product.
     *
     * @param theProduct (The Product object to be created)
     * @return The newly created Product object
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Endpoint to create a new product, returns HTTP 201 status on creation
    public Product addProduct(@RequestBody final Product theProduct) {
        return myProductService.addProduct(theProduct);
    }

    /**
     * Updates an existing product's information.
     *
     * @param theProductID (The ID of the product to update)
     * @param theProductDetails (The Product object containing updated information)
     * @return The updated Product object
     */
    @PutMapping("/{theProductID}") // Endpoint to update a product by its ID
    public Product updateProduct(@PathVariable final Long theProductID, @RequestBody final Product theProductDetails) {
        return myProductService.updateProduct(theProductID, theProductDetails);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param theProductID (The ID of the product to be deleted)
     */
    @DeleteMapping("/{theProductID}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Endpoint to delete a product by its ID, returns HTTP 204 status on successful deletion
    public void deleteProduct(@PathVariable final Long theProductID) {
        myProductService.deleteProduct(theProductID);
    }
}
