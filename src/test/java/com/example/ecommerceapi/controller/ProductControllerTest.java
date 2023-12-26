package com.example.ecommerceapi.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.ecommerceapi.api.controller.ProductController;
import com.example.ecommerceapi.api.dto.ProductDTO;
import com.example.ecommerceapi.api.model.Product;
import com.example.ecommerceapi.api.model.User;
import com.example.ecommerceapi.config.RestExceptionHandler;
import com.example.ecommerceapi.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

@ExtendWith(SpringExtension.class)
public class ProductControllerTest {

    /**
     * The MockMvc instance for performing HTTP request and response simulations.
     */
    private MockMvc myMockMvc;

    /**
     * A mock instance of the ProductService class used for testing purposes.
     */
    @Mock
    private ProductService myProductService;

    /**
     * The ProductController instance with the injected mock ProductService.
     */
    @InjectMocks
    private ProductController myProductController;

    /**
     * A sample ProductDTO used for test cases.
     */
    private ProductDTO myProductDTO;

    /**
     * A sample Product instance used for test cases.
     */
    private Product myProduct;

    /**
     * The ModelMapper instance used for mapping Product to ProductDTO and vice versa.
     */
    private ModelMapper myModelMapper;

    /**
     * Initializes the ModelMapper and UserController, and sets up the MockMvc for testing.
     */
    @BeforeEach
    public void setUp() {
        myModelMapper = new ModelMapper();
        myProductController = new ProductController(myProductService, myModelMapper);
        myMockMvc = MockMvcBuilders.standaloneSetup(myProductController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        // Initialize a Product and UserDTO
        myProduct = new Product("testProduct", BigDecimal.valueOf(99.99), 100);
        myProductDTO = myModelMapper.map(myProduct, ProductDTO.class);
    }

    /**
     * Tests retrieving all products and expects a list of products in the response.
     */
    @Test
    public void whenGetAllProducts_thenReturnProductList() throws Exception {
        // Given: Mock the ProductService to return a list containing a sample product.
        given(myProductService.getAllProducts()).willReturn(Collections.singletonList(myProduct));

        // When: Perform a GET request to "/products" using MockMvc and make assertions.
        myMockMvc.perform(get("/products"))
                .andExpect(status().isOk()) // Expecting a 200 OK status code.
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Expecting the response content type to be JSON.
                .andExpect(jsonPath("$", hasSize(1))) // Expecting the JSON response to be an array with a single element.
                .andExpect(jsonPath("$[0].productName", is(myProductDTO.getProductName()))); // Adjust the JSON path to match your ProductDTO's structure.
    }

    /**
     * Tests retrieving a product by ID and expects a product DTO in the response.
     */
    @Test
    public void whenGetProductByID_thenReturnProductDTO() throws Exception {
        final Long productID = 1L;
        given(myProductService.getProductByID(productID)).willReturn(Optional.of(myProduct));

        myMockMvc.perform(get("/products/" + productID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productName", is(myProductDTO.getProductName())));
    }

    /**
     * Tests adding a product and expects the saved product DTO in the response.
     */
    @Test
    public void whenAddProduct_thenReturnSavedProductDTO() throws Exception {
        given(myProductService.addProduct(org.mockito.ArgumentMatchers.any(Product.class))).willReturn(myProduct);

        final ObjectMapper objectMapper = new ObjectMapper();
        final String productJson = objectMapper.writeValueAsString(myProductDTO);

        myMockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName", is(myProductDTO.getProductName())));
    }

    /**
     * Tests updating a product and expects the updated product DTO in the response.
     */
    @Test
    public void whenDeleteProduct_thenStatusNoContent() throws Exception {
        final Long productID = 1L;
        willDoNothing().given(myProductService).deleteProduct(productID);

        myMockMvc.perform(delete("/products/" + productID))
                .andExpect(status().isNoContent());
    }

    /**
     * Tests retrieving a product by a non-existent ID and expects a Not Found response.
     */
    @Test
    public void whenGetProductByNonExistentID_thenThrowNotFoundException() throws Exception {
        final Long nonExistentProductID = 999L;
        given(myProductService.getProductByID(nonExistentProductID)).willReturn(Optional.empty());

        myMockMvc.perform(get("/products/" + nonExistentProductID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"Product with ID " + nonExistentProductID + " not found\"",
                        result.getResolvedException().getMessage()));
    }

    /**
     * Tests adding a product with invalid data and expects a Bad Request response.
     */
    @Test
    public void whenAddProductWithInvalidData_thenBadRequest() throws Exception {
        final ProductDTO invalidProductDTO = new ProductDTO();
        final String productJson = new ObjectMapper().writeValueAsString(invalidProductDTO);

        myMockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests updating a product with invalid data and expects a Bad Request response.
     */
    @Test
    public void whenUpdateProductWithInvalidData_thenBadRequest() throws Exception {
        final Long productID = 1L;
        final ProductDTO invalidProductDTO = new ProductDTO(); // Invalid DTO
        final String productJson = new ObjectMapper().writeValueAsString(invalidProductDTO);

        myMockMvc.perform(put("/products/" + productID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests retrieving a product with an invalid ID type (non-numeric) and expects a Bad Request response.
     */
    @Test
    public void whenGetProductByInvalidDataType_thenBadRequest() throws Exception {
        myMockMvc.perform(get("/products/abc")) // Non-numeric ID
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests deleting a product with an invalid ID type (non-numeric) and expects a Bad Request response.
     */
    @Test
    public void whenDeleteProductByInvalidIDType_thenBadRequest() throws Exception {
        myMockMvc.perform(delete("/products/abc")) // Non-numeric ID
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests updating a product and expects the updated product DTO in the response.
     */
    @Test
    public void whenUpdateProduct_thenReturnUpdatedProductDTO() throws Exception {
        final Long productID = 1L;
        given(myProductService.updateProduct(eq(productID), org.mockito.ArgumentMatchers.any(Product.class))).willReturn(myProduct);

        final ObjectMapper objectMapper = new ObjectMapper();
        final String productJson = objectMapper.writeValueAsString(myProductDTO);

        myMockMvc.perform(put("/products/" + productID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", is(myProductDTO.getProductName())));
    }


}
