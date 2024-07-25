package com.facialanalyse.facialAPI.models;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.facialanalyse.facialAPI.model.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecommendationModelTest {

    @Mock
    private User mockUser;

    @Mock
    private Product mockProduct;

    @Mock
    private Recommendation mockRecommendation;

    private Recommendation recommendation;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testDateTime = LocalDateTime.now();
        recommendation = new Recommendation(1L, mockUser, mockProduct, testDateTime);
    }

    @Test
    void testRecommendationCreation() {
        assertNotNull(recommendation);
        assertEquals(1L, recommendation.getId());
        assertEquals(mockUser, recommendation.getUser());
        assertEquals(mockProduct, recommendation.getProduct());
        assertEquals(testDateTime, recommendation.getRecommendedAt());
    }

    @Test
    void testSetAndGetId() {
        recommendation.setId(2L);
        assertEquals(2L, recommendation.getId());
    }

    @Test
    void testSetAndGetUser() {
        User newUser = mock(User.class);
        recommendation.setUser(newUser);
        assertEquals(newUser, recommendation.getUser());
    }

    @Test
    void testSetAndGetProduct() {
        Product newProduct = mock(Product.class);
        recommendation.setProduct(newProduct);
        assertEquals(newProduct, recommendation.getProduct());
    }

    @Test
    void testSetAndGetRecommendedAt() {
        LocalDateTime newDateTime = LocalDateTime.now().plusHours(1);
        recommendation.setRecommendedAt(newDateTime);
        assertEquals(newDateTime, recommendation.getRecommendedAt());
    }

    @Test
    void testNullableFields() {
        Recommendation nullRecommendation = new Recommendation();
        assertNull(nullRecommendation.getId());
        assertNull(nullRecommendation.getUser());
        assertNull(nullRecommendation.getProduct());
        assertNull(nullRecommendation.getRecommendedAt());
    }

    @Test
    void testMockRecommendation() {
        when(mockRecommendation.getId()).thenReturn(3L);
        when(mockRecommendation.getUser()).thenReturn(mockUser);
        when(mockRecommendation.getProduct()).thenReturn(mockProduct);
        when(mockRecommendation.getRecommendedAt()).thenReturn(testDateTime);

        assertEquals(3L, mockRecommendation.getId());
        assertEquals(mockUser, mockRecommendation.getUser());
        assertEquals(mockProduct, mockRecommendation.getProduct());
        assertEquals(testDateTime, mockRecommendation.getRecommendedAt());

        verify(mockRecommendation, times(1)).getId();
        verify(mockRecommendation, times(1)).getUser();
        verify(mockRecommendation, times(1)).getProduct();
        verify(mockRecommendation, times(1)).getRecommendedAt();
    }

    @Test
    void testUserAssociation() {
        when(mockUser.getId()).thenReturn(1L);

        Recommendation recommendationWithUser = new Recommendation(1L, mockUser, mockProduct, testDateTime);
        assertEquals(mockUser, recommendationWithUser.getUser());
        assertEquals(1L, recommendationWithUser.getUser().getId());

        verify(mockUser, times(1)).getId();
    }

    @Test
    void testProductAssociation() {
        when(mockProduct.getId()).thenReturn(1L);
        when(mockProduct.getName()).thenReturn("Test Product");

        Recommendation recommendationWithProduct = new Recommendation(1L, mockUser, mockProduct, testDateTime);
        assertEquals(mockProduct, recommendationWithProduct.getProduct());
        assertEquals(1L, recommendationWithProduct.getProduct().getId());
        assertEquals("Test Product", recommendationWithProduct.getProduct().getName());

        verify(mockProduct, times(1)).getId();
        verify(mockProduct, times(1)).getName();
    }
}