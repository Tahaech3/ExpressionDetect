package com.facialanalyse.facialAPI.services;

import com.facialanalyse.facialAPI.model.Product;
import com.facialanalyse.facialAPI.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.facialanalyse.facialAPI.service.ProductService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProducts() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Product 1", "Description 1", 10.0),
                new Product(2L, "Product 2", "Description 2", 20.0)
        );
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());
    }

    @Test
    void getProductById() {
        Product product = new Product(1L, "Product 1", "Description 1", 10.0);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals("Product 1", result.get().getName());
    }

    @Test
    void createProduct() {
        Product product = new Product(null, "New Product", "New Description", 15.0);
        Product savedProduct = new Product(1L, "New Product", "New Description", 15.0);
        when(productRepository.save(product)).thenReturn(savedProduct);

        Product result = productService.createProduct(product);

        assertEquals(1L, result.getId());
        assertEquals("New Product", result.getName());
    }

    @Test
    void updateProduct() {
        Product existingProduct = new Product(1L, "Old Product", "Old Description", 10.0);
        Product updatedProduct = new Product(1L, "Updated Product", "Updated Description", 15.0);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Optional<Product> result = productService.updateProduct(1L, updatedProduct);

        assertTrue(result.isPresent());
        assertEquals("Updated Product", result.get().getName());
        assertEquals("Updated Description", result.get().getDescription());
        assertEquals(15.0, result.get().getPrice());
    }

    @Test
    void deleteProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);

        boolean result = productService.deleteProduct(1L);

        assertTrue(result);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void searchProductsByName() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Product 1", "Description 1", 10.0),
                new Product(2L, "Product 2", "Description 2", 20.0)
        );
        when(productRepository.findByNameContainingIgnoreCase("Product")).thenReturn(products);

        List<Product> result = productService.searchProductsByName("Product");

        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());
    }

    @Test
    void searchProductsByDescription() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Product 1", "Description 1", 10.0),
                new Product(2L, "Product 2", "Description 2", 20.0)
        );
        when(productRepository.findByDescriptionContainingIgnoreCase("Description")).thenReturn(products);

        List<Product> result = productService.searchProductsByDescription("Description");

        assertEquals(2, result.size());
        assertEquals("Description 1", result.get(0).getDescription());
        assertEquals("Description 2", result.get(1).getDescription());
    }

    @Test
    void getProductsInPriceRange() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Product 1", "Description 1", 10.0),
                new Product(2L, "Product 2", "Description 2", 20.0)
        );
        when(productRepository.findByPriceBetween(5.0, 25.0)).thenReturn(products);

        List<Product> result = productService.getProductsInPriceRange(5.0, 25.0);

        assertEquals(2, result.size());
        assertEquals(10.0, result.get(0).getPrice());
        assertEquals(20.0, result.get(1).getPrice());
    }

    @Test
    void searchProductsByNameSortedByPrice() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Product 1", "Description 1", 10.0),
                new Product(2L, "Product 2", "Description 2", 20.0)
        );
        when(productRepository.findByNameContainingIgnoreCaseOrderByPriceAsc("Product")).thenReturn(products);

        List<Product> result = productService.searchProductsByNameSortedByPrice("Product");

        assertEquals(2, result.size());
        assertEquals(10.0, result.get(0).getPrice());
        assertEquals(20.0, result.get(1).getPrice());
    }
}