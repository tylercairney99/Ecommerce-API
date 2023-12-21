package com.example.ecommerceapi.api.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.ecommerceapi.service.ProductService;
import com.example.ecommerceapi.api.model.Product;
import com.example.ecommerceapi.api.dto.ProductDTO;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.modelmapper.ModelMapper;
import jakarta.validation.Valid;

import java.util.*;
import java.util.stream.Collectors;

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
     * ModelMapper for converting between DTO and entity classes.
     */
    private final ModelMapper myModelMapper;

    /**
     * Constructs a ProductController with dependency injection of the ProductService.
     *
     * @param theProductService (The service handling product-related business logic)
     */
    @Autowired // Automatically injects an instance of ProductService
    public ProductController(final ProductService theProductService, final ModelMapper theModelMapper) {
        this.myProductService = theProductService;
        this.myModelMapper = theModelMapper;
    }

    /**
     * Retrieves all products and returns them as a list of ProductDTOs.
     *
     * This method fetches a list of all Product entities, maps them to ProductDTOs,
     * and returns the list of DTOs. Useful for providing a view of product data
     * suitable for client-side use.
     *
     * @return A list of ProductDTOs representing all products.
     */
    @GetMapping // Endpoint for getting all products
    public List<ProductDTO> getAllProducts() {
        final List<Product> products = myProductService.getAllProducts();
        return products.stream()
                .map(product -> myModelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific product by its unique identifier and returns it as a ProductDTO.
     *
     * This method fetches a product from the database using its ID. If found, the product
     * is mapped to a ProductDTO and returned. If no product is found with the provided ID,
     * a ResponseStatusException with HttpStatus.NOT_FOUND is thrown.
     *
     * @param theProductID (The unique identifier of the product to be retrieved)
     * @return A ProductDTO representing the retrieved product.
     * @throws ResponseStatusException If no product with the given ID is found.
     */
    @GetMapping("/{theProductID}") // Endpoint to get a product by its ID
    public ProductDTO getProductByID(@PathVariable final Long theProductID) {
        final Product product = myProductService.getProductByID(theProductID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with ID " + theProductID + " not found"));
        return myModelMapper.map(product, ProductDTO.class);
    }

    /**
     * Creates a new product from the provided ProductDTO.
     *
     * Accepts a ProductDTO object with product details, validates it, maps it to a Product entity,
     * and persists it in the database. The newly created product is then mapped back to a ProductDTO
     * and returned. This method generates an HTTP 201 status code upon the successful creation of a product.
     *
     * @param theProductDTO (The DTO containing the new product's details)
     * @return The ProductDTO of the newly created product.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Endpoint to create a new product, returns HTTP 201 status on creation
    public ProductDTO addProduct(@Valid @RequestBody final ProductDTO theProductDTO) {
        final Product product = myModelMapper.map(theProductDTO, Product.class);
        final Product createdProduct = myProductService.addProduct(product);
        return myModelMapper.map(createdProduct, ProductDTO.class);
    }

    /**
     * Updates an existing product's details based on the provided ProductDTO.
     *
     * This method accepts a ProductDTO object containing updated product information, along with the product's ID.
     * The ProductDTO is validated and then mapped to a Product entity, which is used to update the existing product
     * in the database. The updated product is then mapped back to a ProductDTO and returned.
     *
     * @param theProductID (The ID of the product to be updated)
     * @param theProductDTO (The DTO containing the updated details for the product)
     * @return The ProductDTO representing the updated product.
     */
    @PutMapping("/{theProductID}")
    public ProductDTO updateProduct(@PathVariable Long theProductID, @Valid @RequestBody final ProductDTO theProductDTO) {
        final Product productToUpdate = myModelMapper.map(theProductDTO, Product.class);
        final Product updatedProduct = myProductService.updateProduct(theProductID, productToUpdate);
        return myModelMapper.map(updatedProduct, ProductDTO.class);
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
