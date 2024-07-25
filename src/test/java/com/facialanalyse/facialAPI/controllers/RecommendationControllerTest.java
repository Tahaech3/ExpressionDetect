package com.facialanalyse.facialAPI.controllers;

import com.facialanalyse.facialAPI.model.Recommendation;
import com.facialanalyse.facialAPI.model.User;
import com.facialanalyse.facialAPI.model.Product;
import com.facialanalyse.facialAPI.service.RecommendationService;
import com.facialanalyse.facialAPI.service.UserService;
import com.facialanalyse.facialAPI.service.ProductService;
import com.facialanalyse.facialAPI.controller.RecommendationController;
import com.facialanalyse.facialAPI.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecommendationControllerTest {

    @Mock
    private RecommendationService recommendationService;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private RecommendationController recommendationController;

    private Recommendation testRecommendation;
    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");

        testRecommendation = new Recommendation();
        testRecommendation.setId(1L);
        testRecommendation.setUser(testUser);
        testRecommendation.setProduct(testProduct);
        testRecommendation.setRecommendedAt(LocalDateTime.now());
    }

    @Test
    void getAllRecommendations() {
        when(recommendationService.getAllRecommendations()).thenReturn(Arrays.asList(testRecommendation));

        ResponseEntity<List<Recommendation>> response = recommendationController.getAllRecommendations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testRecommendation, response.getBody().get(0));
        verify(recommendationService).getAllRecommendations();
    }

    @Test
    void getRecommendationById() {
        when(recommendationService.getRecommendationById(1L)).thenReturn(Optional.of(testRecommendation));

        ResponseEntity<Recommendation> response = recommendationController.getRecommendationById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testRecommendation, response.getBody());
        verify(recommendationService).getRecommendationById(1L);
    }

    @Test
    void getRecommendationById_NotFound() {
        when(recommendationService.getRecommendationById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Recommendation> response = recommendationController.getRecommendationById(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(recommendationService).getRecommendationById(2L);
    }

    @Test
    void createRecommendation() {
        when(recommendationService.createRecommendation(any(Recommendation.class))).thenReturn(testRecommendation);

        ResponseEntity<Recommendation> response = recommendationController.createRecommendation(testRecommendation);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testRecommendation, response.getBody());
        verify(recommendationService).createRecommendation(testRecommendation);
    }

    @Test
    void updateRecommendation() {
        Recommendation updatedRecommendation = new Recommendation();
        updatedRecommendation.setId(1L);
        updatedRecommendation.setUser(testUser);
        updatedRecommendation.setProduct(testProduct);
        updatedRecommendation.setRecommendedAt(LocalDateTime.now().plusDays(1));

        when(recommendationService.updateRecommendation(eq(1L), any(Recommendation.class))).thenReturn(Optional.of(updatedRecommendation));

        ResponseEntity<Recommendation> response = recommendationController.updateRecommendation(1L, updatedRecommendation);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedRecommendation, response.getBody());
        verify(recommendationService).updateRecommendation(eq(1L), any(Recommendation.class));
    }

    @Test
    void updateRecommendation_NotFound() {
        Recommendation updatedRecommendation = new Recommendation();
        when(recommendationService.updateRecommendation(eq(2L), any(Recommendation.class))).thenReturn(Optional.empty());

        ResponseEntity<Recommendation> response = recommendationController.updateRecommendation(2L, updatedRecommendation);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(recommendationService).updateRecommendation(eq(2L), any(Recommendation.class));
    }

    @Test
    void deleteRecommendation() {
        when(recommendationService.deleteRecommendation(1L)).thenReturn(true);

        ResponseEntity<Void> response = recommendationController.deleteRecommendation(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(recommendationService).deleteRecommendation(1L);
    }

    @Test
    void deleteRecommendation_NotFound() {
        when(recommendationService.deleteRecommendation(2L)).thenReturn(false);

        ResponseEntity<Void> response = recommendationController.deleteRecommendation(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(recommendationService).deleteRecommendation(2L);
    }

    @Test
    void getRecommendationsForUser() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));
        when(recommendationService.getRecommendationsForUser(testUser)).thenReturn(Arrays.asList(testRecommendation));

        ResponseEntity<List<Recommendation>> response = recommendationController.getRecommendationsForUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testRecommendation, response.getBody().get(0));
        verify(userService).getUserById(1L);
        verify(recommendationService).getRecommendationsForUser(testUser);
    }

    @Test
    void getRecommendationsForUser_UserNotFound() {
        when(userService.getUserById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recommendationController.getRecommendationsForUser(2L));
        verify(userService).getUserById(2L);
    }

    @Test
    void getRecommendationsForProduct() {
        when(productService.getProductById(1L)).thenReturn(Optional.of(testProduct));
        when(recommendationService.getRecommendationsForProduct(testProduct)).thenReturn(Arrays.asList(testRecommendation));

        ResponseEntity<List<Recommendation>> response = recommendationController.getRecommendationsForProduct(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testRecommendation, response.getBody().get(0));
        verify(productService).getProductById(1L);
        verify(recommendationService).getRecommendationsForProduct(testProduct);
    }

    @Test
    void getRecommendationsForProduct_ProductNotFound() {
        when(productService.getProductById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recommendationController.getRecommendationsForProduct(2L));
        verify(productService).getProductById(2L);
    }

    @Test
    void getRecommendationsByDateRange() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();

        when(recommendationService.getRecommendationsByDateRange(start, end)).thenReturn(Arrays.asList(testRecommendation));

        ResponseEntity<List<Recommendation>> response = recommendationController.getRecommendationsByDateRange(start, end);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testRecommendation, response.getBody().get(0));
        verify(recommendationService).getRecommendationsByDateRange(start, end);
    }

    @Test
    void getRecommendationsForUserByDateRange() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();

        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));
        when(recommendationService.getRecommendationsForUserByDateRange(testUser, start, end)).thenReturn(Arrays.asList(testRecommendation));

        ResponseEntity<List<Recommendation>> response = recommendationController.getRecommendationsForUserByDateRange(1L, start, end);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testRecommendation, response.getBody().get(0));
        verify(userService).getUserById(1L);
        verify(recommendationService).getRecommendationsForUserByDateRange(testUser, start, end);
    }

    @Test
    void getRecommendationsForUserByDateRange_UserNotFound() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();

        when(userService.getUserById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recommendationController.getRecommendationsForUserByDateRange(2L, start, end));
        verify(userService).getUserById(2L);
    }

    @Test
    void countRecommendationsForProduct() {
        when(productService.getProductById(1L)).thenReturn(Optional.of(testProduct));
        when(recommendationService.countRecommendationsForProduct(testProduct)).thenReturn(5L);

        ResponseEntity<Long> response = recommendationController.countRecommendationsForProduct(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody());
        verify(productService).getProductById(1L);
        verify(recommendationService).countRecommendationsForProduct(testProduct);
    }

    @Test
    void countRecommendationsForProduct_ProductNotFound() {
        when(productService.getProductById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recommendationController.countRecommendationsForProduct(2L));
        verify(productService).getProductById(2L);
    }

    @Test
    void getRecentRecommendationsForUser() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));
        when(recommendationService.getRecentRecommendationsForUser(testUser, 5)).thenReturn(Arrays.asList(testRecommendation));

        ResponseEntity<List<Recommendation>> response = recommendationController.getRecentRecommendationsForUser(1L, 5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testRecommendation, response.getBody().get(0));
        verify(userService).getUserById(1L);
        verify(recommendationService).getRecentRecommendationsForUser(testUser, 5);
    }

    @Test
    void getRecentRecommendationsForUser_UserNotFound() {
        when(userService.getUserById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recommendationController.getRecentRecommendationsForUser(2L, 5));
        verify(userService).getUserById(2L);
    }
}