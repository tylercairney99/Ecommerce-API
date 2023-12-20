package com.example.ecommerceapi.service;

import com.example.ecommerceapi.api.model.Product;
import com.example.ecommerceapi.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private final ProductRepository productRepository;

    /**
     * Constructs a ProductService with the provided product repository.
     *
     * @param theProductRepository The product repository to be used by this service.
     */
    @Autowired
    public ProductService(final ProductRepository theProductRepository) {
        this.productRepository = theProductRepository;
    }

    /**
     * Adds a new product to the repository.
     *
     * @param theProduct (The product to be added)
     * @return The added product
     */
    public Product addProduct(final Product theProduct) { // Create
        return productRepository.save(theProduct);
    }

    /**
     * Retrieve all products in the repository.
     *
     * @return A list of all products
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Retrieves a specific product based on its ID.
     *
     * @param theID (The ID of the product)
     * @return An Optional containing the product if found.
     */
    public Optional<Product> getProductByID(final Long theID) { // Read
        return productRepository.findById(theID);
    }

    /**
     * Updates a product's details in the repository.
     * Throws IllegalArgumentException if product is not found.
     *
     * @param theID The ID of the product to update.
     * @param productDetails The new details for the product.
     * @return The updated product.
     * @throws IllegalArgumentException If no product with the given ID is found.
     */
    public Product updateProduct(final Long theID, final Product productDetails) { // Update
        final Product product = productRepository.findById(theID)
                .orElseThrow(() -> new IllegalArgumentException("Product with ID " + theID + " not found"));

        product.setProductName(productDetails.getProductName());
        product.setPrice(productDetails.getPrice());
        product.setStockQuantity(productDetails.getStockQuantity());

        // can add more logic to change products here

        return productRepository.save(product);
    }

    /**
     * Deletes a product from the repository by its ID.
     * Throws IllegalArgumentException if product is not found.
     *
     * @param theID The ID of the product to delete.
     * @throws IllegalArgumentException If no product with the given ID is found.
     */
    public void deleteProduct(final Long theID) {
        Product product = productRepository.findById(theID)
                .orElseThrow(() -> new IllegalArgumentException("Product with ID " + theID + " not found"));

        productRepository.delete(product);
    }
}

