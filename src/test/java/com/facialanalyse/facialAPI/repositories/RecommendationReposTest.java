package com.facialanalyse.facialAPI.repositories;

import com.facialanalyse.facialAPI.model.Recommendation;
import com.facialanalyse.facialAPI.model.User;
import com.facialanalyse.facialAPI.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RecommendationReposTest {

    @Mock
    private RecommendationRepository recommendationRepository;

    private User testUser;
    private Product testProduct;
    private Recommendation testRecommendation1;
    private Recommendation testRecommendation2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);

        testProduct = new Product();
        testProduct.setId(1L);

        testRecommendation1 = new Recommendation();
        testRecommendation1.setId(1L);
        testRecommendation1.setUser(testUser);
        testRecommendation1.setProduct(testProduct);
        testRecommendation1.setRecommendedAt(LocalDateTime.now());

        testRecommendation2 = new Recommendation();
        testRecommendation2.setId(2L);
        testRecommendation2.setUser(testUser);
        testRecommendation2.setProduct(testProduct);
        testRecommendation2.setRecommendedAt(LocalDateTime.now().plusHours(1));
    }

    @Test
    public void testFindByUser() {
        List<Recommendation> recommendations = Arrays.asList(testRecommendation1, testRecommendation2);
        when(recommendationRepository.findByUser(testUser)).thenReturn(recommendations);

        List<Recommendation> result = recommendationRepository.findByUser(testUser);

        assertEquals(2, result.size());
        verify(recommendationRepository).findByUser(testUser);
    }

    @Test
    public void testFindByProduct() {
        List<Recommendation> recommendations = Arrays.asList(testRecommendation1, testRecommendation2);
        when(recommendationRepository.findByProduct(testProduct)).thenReturn(recommendations);

        List<Recommendation> result = recommendationRepository.findByProduct(testProduct);

        assertEquals(2, result.size());
        verify(recommendationRepository).findByProduct(testProduct);
    }

    @Test
    public void testFindByRecommendedAtBetween() {
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);
        List<Recommendation> recommendations = Arrays.asList(testRecommendation1, testRecommendation2);
        when(recommendationRepository.findByRecommendedAtBetween(start, end)).thenReturn(recommendations);

        List<Recommendation> result = recommendationRepository.findByRecommendedAtBetween(start, end);

        assertEquals(2, result.size());
        verify(recommendationRepository).findByRecommendedAtBetween(start, end);
    }

    @Test
    public void testFindByUserAndRecommendedAtBetween() {
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);
        List<Recommendation> recommendations = Arrays.asList(testRecommendation1, testRecommendation2);
        when(recommendationRepository.findByUserAndRecommendedAtBetween(testUser, start, end)).thenReturn(recommendations);

        List<Recommendation> result = recommendationRepository.findByUserAndRecommendedAtBetween(testUser, start, end);

        assertEquals(2, result.size());
        verify(recommendationRepository).findByUserAndRecommendedAtBetween(testUser, start, end);
    }

    @Test
    public void testCountByProduct() {
        when(recommendationRepository.countByProduct(testProduct)).thenReturn(2L);

        long count = recommendationRepository.countByProduct(testProduct);

        assertEquals(2L, count);
        verify(recommendationRepository).countByProduct(testProduct);
    }
}