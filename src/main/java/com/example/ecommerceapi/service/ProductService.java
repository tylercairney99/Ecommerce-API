package com.example.ecommerceapi.service;


import com.example.ecommerceapi.api.model.Product;
import com.example.ecommerceapi.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.*;

/**
 * Service class for handling product operations.
 * This class provides CRUD (Create, Read, Update, Delete) operations for products.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
@Service
public class ProductService {

    /**
     * The repository for product data.
     */
    private final ProductRepository myProductRepository;

    /**
     * Constructs a ProductService with the provided product repository.
     *
     *
     * @param theProductRepository The product repository to be used by this service.
     */
    @Autowired
    public ProductService(final ProductRepository theProductRepository) {
        this.myProductRepository = theProductRepository;
    }

    /**
     * Adds a new product to the repository.
     *
     * @param theProduct (The product to be added)
     * @return The added product
     */
    public Product addProduct(final Product theProduct) { // Create
        return myProductRepository.save(theProduct);
    }

    /**
     * Retrieve all products in the repository.
     *
     * @return A list of all products
     */
    public List<Product> getAllProducts() { // Read
        return myProductRepository.findAll();
    }

    /**
     * Retrieves a specific product based on its ID.
     *
     * @param theID (The ID of the product)
     * @return An Optional containing the product if found
     */
    public Optional<Product> getProductByID(final Long theID) { // Read
        return myProductRepository.findById(theID);
    }

    /**
     * Updates a product's details in the repository.
     * Throws IllegalArgumentException if product is not found.
     *
     * @param theProductID (The ID of the product to update)
     * @param theProductDetails (The new details for the product)
     * @return The updated product
     * @throws ResponseStatusException If no product with the given ID is found
     */
    public Product updateProduct(final Long theProductID, final Product theProductDetails) { // Update
        final Product product = myProductRepository.findById(theProductID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with ID " + theProductID + " not found"));

        if (theProductDetails.getProductName() != null) {
            product.setProductName(theProductDetails.getProductName());
        }
        if (theProductDetails.getPrice() != null) {
            product.setPrice(theProductDetails.getPrice());
        }
        product.setStockQuantity(theProductDetails.getStockQuantity()); // not checking for null because stockQuantity is a primitive (int)

        // can add more logic to change products here

        return myProductRepository.save(product);
    }

    /**
     * Deletes a product from the repository by its ID.
     * Throws IllegalArgumentException if product is not found.
     *
     * @param theProductID (The ID of the product to delete)
     * @throws ResponseStatusException If no product with the given ID is found
     */
    public void deleteProduct(final Long theProductID) { // Delete
        final Product product = myProductRepository.findById(theProductID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with ID " + theProductID + " not found"));

        myProductRepository.delete(product);
    }
}

