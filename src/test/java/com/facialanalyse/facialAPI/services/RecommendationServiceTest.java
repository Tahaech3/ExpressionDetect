package com.facialanalyse.facialAPI.services;

import com.facialanalyse.facialAPI.model.Product;
import com.facialanalyse.facialAPI.model.Recommendation;
import com.facialanalyse.facialAPI.model.User;
import com.facialanalyse.facialAPI.repositories.RecommendationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.facialanalyse.facialAPI.service.RecommendationService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecommendationServiceTest {

    @Mock
    private RecommendationRepository recommendationRepository;

    @InjectMocks
    private RecommendationService recommendationService;

    private User testUser;
    private Product testProduct;
    private Recommendation testRecommendation;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User(1L, "testUser", "test@example.com");
        testProduct = new Product(1L, "Test Product", "Description", 99.99);
        testDateTime = LocalDateTime.now();
        testRecommendation = new Recommendation(1L, testUser, testProduct, testDateTime);
    }

    @Test
    void getAllRecommendations() {
        List<Recommendation> recommendations = Arrays.asList(
                testRecommendation,
                new Recommendation(2L, testUser, testProduct, testDateTime.plusHours(1))
        );
        when(recommendationRepository.findAll()).thenReturn(recommendations);

        List<Recommendation> result = recommendationService.getAllRecommendations();

        assertEquals(2, result.size());
        assertEquals(testUser, result.get(0).getUser());
        assertEquals(testProduct, result.get(1).getProduct());
    }

    @Test
    void getRecommendationById() {
        when(recommendationRepository.findById(1L)).thenReturn(Optional.of(testRecommendation));

        Optional<Recommendation> result = recommendationService.getRecommendationById(1L);

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get().getUser());
    }

    @Test
    void createRecommendation() {
        when(recommendationRepository.save(any(Recommendation.class))).thenReturn(testRecommendation);

        Recommendation result = recommendationService.createRecommendation(testRecommendation);

        assertEquals(1L, result.getId());
        assertEquals(testUser, result.getUser());
    }

    @Test
    void updateRecommendation() {
        Recommendation updatedRecommendation = new Recommendation(1L, testUser, testProduct, testDateTime.plusHours(2));
        when(recommendationRepository.findById(1L)).thenReturn(Optional.of(testRecommendation));
        when(recommendationRepository.save(any(Recommendation.class))).thenReturn(updatedRecommendation);

        Optional<Recommendation> result = recommendationService.updateRecommendation(1L, updatedRecommendation);

        assertTrue(result.isPresent());
        assertEquals(testDateTime.plusHours(2), result.get().getRecommendedAt());
    }

    @Test
    void deleteRecommendation() {
        when(recommendationRepository.existsById(1L)).thenReturn(true);

        boolean result = recommendationService.deleteRecommendation(1L);

        assertTrue(result);
        verify(recommendationRepository, times(1)).deleteById(1L);
    }

    @Test
    void getRecommendationsForUser() {
        List<Recommendation> recommendations = Arrays.asList(
                testRecommendation,
                new Recommendation(2L, testUser, testProduct, testDateTime.plusHours(1))
        );
        when(recommendationRepository.findByUser(testUser)).thenReturn(recommendations);

        List<Recommendation> result = recommendationService.getRecommendationsForUser(testUser);

        assertEquals(2, result.size());
        assertEquals(testUser, result.get(0).getUser());
        assertEquals(testUser, result.get(1).getUser());
    }

    @Test
    void getRecommendationsForProduct() {
        List<Recommendation> recommendations = Arrays.asList(
                testRecommendation,
                new Recommendation(2L, testUser, testProduct, testDateTime.plusHours(1))
        );
        when(recommendationRepository.findByProduct(testProduct)).thenReturn(recommendations);

        List<Recommendation> result = recommendationService.getRecommendationsForProduct(testProduct);

        assertEquals(2, result.size());
        assertEquals(testProduct, result.get(0).getProduct());
        assertEquals(testProduct, result.get(1).getProduct());
    }

    @Test
    void getRecommendationsByDateRange() {
        LocalDateTime start = testDateTime.minusHours(1);
        LocalDateTime end = testDateTime.plusHours(2);
        List<Recommendation> recommendations = Arrays.asList(
                testRecommendation,
                new Recommendation(2L, testUser, testProduct, testDateTime.plusHours(1))
        );
        when(recommendationRepository.findByRecommendedAtBetween(start, end)).thenReturn(recommendations);

        List<Recommendation> result = recommendationService.getRecommendationsByDateRange(start, end);

        assertEquals(2, result.size());
        assertEquals(testDateTime, result.get(0).getRecommendedAt());
        assertEquals(testDateTime.plusHours(1), result.get(1).getRecommendedAt());
    }

    @Test
    void getRecommendationsForUserByDateRange() {
        LocalDateTime start = testDateTime.minusHours(1);
        LocalDateTime end = testDateTime.plusHours(2);
        List<Recommendation> recommendations = Arrays.asList(
                testRecommendation,
                new Recommendation(2L, testUser, testProduct, testDateTime.plusHours(1))
        );
        when(recommendationRepository.findByUserAndRecommendedAtBetween(testUser, start, end)).thenReturn(recommendations);

        List<Recommendation> result = recommendationService.getRecommendationsForUserByDateRange(testUser, start, end);

        assertEquals(2, result.size());
        assertEquals(testUser, result.get(0).getUser());
        assertEquals(testUser, result.get(1).getUser());
    }

    @Test
    void countRecommendationsForProduct() {
        when(recommendationRepository.countByProduct(testProduct)).thenReturn(5L);

        long result = recommendationService.countRecommendationsForProduct(testProduct);

        assertEquals(5L, result);
    }

    @Test
    void getRecentRecommendationsForUser() {
        List<Recommendation> recommendations = Arrays.asList(
                testRecommendation,
                new Recommendation(2L, testUser, testProduct, testDateTime.plusHours(1)),
                new Recommendation(3L, testUser, testProduct, testDateTime.plusHours(2))
        );
        when(recommendationRepository.findByUser(testUser)).thenReturn(recommendations);

        List<Recommendation> result = recommendationService.getRecentRecommendationsForUser(testUser, 2);

        assertEquals(2, result.size());
        assertEquals(testDateTime.plusHours(2), result.get(0).getRecommendedAt());
        assertEquals(testDateTime.plusHours(1), result.get(1).getRecommendedAt());
    }
}
