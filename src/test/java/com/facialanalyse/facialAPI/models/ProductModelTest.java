package com.facialanalyse.facialAPI.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.facialanalyse.facialAPI.model.Product;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductModelTest {

    @Mock
    private Product mockProduct;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product(1L, "Test Product", "Test Description", 9.99);
    }

    @Test
    void testProductCreation() {
        assertNotNull(product);
        assertEquals(1L, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals("Test Description", product.getDescription());
        assertEquals(9.99, product.getPrice());
    }

    @Test
    void testSetAndGetId() {
        product.setId(2L);
        assertEquals(2L, product.getId());
    }

    @Test
    void testSetAndGetName() {
        product.setName("New Product Name");
        assertEquals("New Product Name", product.getName());
    }

    @Test
    void testSetAndGetDescription() {
        product.setDescription("New Description");
        assertEquals("New Description", product.getDescription());
    }

    @Test
    void testSetAndGetPrice() {
        product.setPrice(19.99);
        assertEquals(19.99, product.getPrice());
    }

    @Test
    void testNullableFields() {
        Product nullProduct = new Product();
        assertNull(nullProduct.getId());
        assertNull(nullProduct.getName());
        assertNull(nullProduct.getDescription());
        assertNull(nullProduct.getPrice());
    }

    @Test
    void testMockProduct() {
        when(mockProduct.getId()).thenReturn(3L);
        when(mockProduct.getName()).thenReturn("Mock Product");
        when(mockProduct.getDescription()).thenReturn("Mock Description");
        when(mockProduct.getPrice()).thenReturn(29.99);

        assertEquals(3L, mockProduct.getId());
        assertEquals("Mock Product", mockProduct.getName());
        assertEquals("Mock Description", mockProduct.getDescription());
        assertEquals(29.99, mockProduct.getPrice());

        verify(mockProduct, times(1)).getId();
        verify(mockProduct, times(1)).getName();
        verify(mockProduct, times(1)).getDescription();
        verify(mockProduct, times(1)).getPrice();
    }
}