package com.facialanalyse.facialAPI.controllers;

import com.facialanalyse.facialAPI.model.Product;
import com.facialanalyse.facialAPI.service.ProductService;
import com.facialanalyse.facialAPI.controller.ProductController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(9.99);
    }

    @Test
    void getAllProducts() {
        when(productService.getAllProducts()).thenReturn(Arrays.asList(testProduct));

        ResponseEntity<List<Product>> response = productController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testProduct, response.getBody().get(0));
        verify(productService).getAllProducts();
    }

    @Test
    void getProductById() {
        when(productService.getProductById(1L)).thenReturn(Optional.of(testProduct));

        ResponseEntity<Product> response = productController.getProductById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testProduct, response.getBody());
        verify(productService).getProductById(1L);
    }

    @Test
    void getProductById_NotFound() {
        when(productService.getProductById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.getProductById(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService).getProductById(2L);
    }

    @Test
    void createProduct() {
        when(productService.createProduct(any(Product.class))).thenReturn(testProduct);

        ResponseEntity<Product> response = productController.createProduct(testProduct);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testProduct, response.getBody());
        verify(productService).createProduct(testProduct);
    }

    @Test
    void updateProduct() {
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Description");

        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(Optional.of(updatedProduct));

        ResponseEntity<Product> response = productController.updateProduct(1L, updatedProduct);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProduct, response.getBody());
        verify(productService).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    void updateProduct_NotFound() {
        Product updatedProduct = new Product();
        when(productService.updateProduct(eq(2L), any(Product.class))).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.updateProduct(2L, updatedProduct);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService).updateProduct(eq(2L), any(Product.class));
    }

    @Test
    void deleteProduct() {
        when(productService.deleteProduct(1L)).thenReturn(true);

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productService).deleteProduct(1L);
    }

    @Test
    void deleteProduct_NotFound() {
        when(productService.deleteProduct(2L)).thenReturn(false);

        ResponseEntity<Void> response = productController.deleteProduct(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productService).deleteProduct(2L);
    }

    @Test
    void searchProducts_ByName() {
        when(productService.searchProductsByName("Test")).thenReturn(Arrays.asList(testProduct));

        ResponseEntity<List<Product>> response = productController.searchProducts("Test", null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testProduct, response.getBody().get(0));
        verify(productService).searchProductsByName("Test");
    }

    @Test
    void searchProducts_ByDescription() {
        when(productService.searchProductsByDescription("Description")).thenReturn(Arrays.asList(testProduct));

        ResponseEntity<List<Product>> response = productController.searchProducts(null, "Description");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testProduct, response.getBody().get(0));
        verify(productService).searchProductsByDescription("Description");
    }

    @Test
    void searchProducts_NoParams() {
        when(productService.getAllProducts()).thenReturn(Arrays.asList(testProduct));

        ResponseEntity<List<Product>> response = productController.searchProducts(null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testProduct, response.getBody().get(0));
        verify(productService).getAllProducts();
    }

    @Test
    void getProductsInPriceRange() {
        when(productService.getProductsInPriceRange(0.0, 10.0)).thenReturn(Arrays.asList(testProduct));

        ResponseEntity<List<Product>> response = productController.getProductsInPriceRange(0.0, 10.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testProduct, response.getBody().get(0));
        verify(productService).getProductsInPriceRange(0.0, 10.0);
    }

    @Test
    void searchProductsSortedByPrice() {
        when(productService.searchProductsByNameSortedByPrice("Test")).thenReturn(Arrays.asList(testProduct));

        ResponseEntity<List<Product>> response = productController.searchProductsSortedByPrice("Test");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testProduct, response.getBody().get(0));
        verify(productService).searchProductsByNameSortedByPrice("Test");
    }
}