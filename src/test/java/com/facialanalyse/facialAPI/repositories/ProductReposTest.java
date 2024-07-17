package com.facialanalyse.facialAPI.repositories;

import com.facialanalyse.facialAPI.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductReposTest {

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByNameContainingIgnoreCase() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Product 1", "Description 1", 10.0),
                new Product(2L, "Product 2", "Description 2", 20.0)
        );
        when(productRepository.findByNameContainingIgnoreCase("product")).thenReturn(products);

        List<Product> result = productRepository.findByNameContainingIgnoreCase("product");

        assertEquals(2, result.size());
        verify(productRepository).findByNameContainingIgnoreCase("product");
    }

    @Test
    public void testFindByDescriptionContainingIgnoreCase() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Product 1", "Test Description", 10.0),
                new Product(2L, "Product 2", "Another Test Description", 20.0)
        );
        when(productRepository.findByDescriptionContainingIgnoreCase("test")).thenReturn(products);

        List<Product> result = productRepository.findByDescriptionContainingIgnoreCase("test");

        assertEquals(2, result.size());
        verify(productRepository).findByDescriptionContainingIgnoreCase("test");
    }

    @Test
    public void testFindByPriceBetween() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Product 1", "Description 1", 15.0),
                new Product(2L, "Product 2", "Description 2", 25.0)
        );
        when(productRepository.findByPriceBetween(10.0, 30.0)).thenReturn(products);

        List<Product> result = productRepository.findByPriceBetween(10.0, 30.0);

        assertEquals(2, result.size());
        verify(productRepository).findByPriceBetween(10.0, 30.0);
    }

    @Test
    public void testFindByNameContainingIgnoreCaseOrderByPriceAsc() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Cheap Product", "Description 1", 10.0),
                new Product(2L, "Expensive Product", "Description 2", 20.0)
        );
        when(productRepository.findByNameContainingIgnoreCaseOrderByPriceAsc("product")).thenReturn(products);

        List<Product> result = productRepository.findByNameContainingIgnoreCaseOrderByPriceAsc("product");

        assertEquals(2, result.size());
        assertTrue(result.get(0).getPrice() < result.get(1).getPrice());
        verify(productRepository).findByNameContainingIgnoreCaseOrderByPriceAsc("product");
    }
}