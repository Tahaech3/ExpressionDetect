package com.facialanalyse.facialAPI.service;

import com.facialanalyse.facialAPI.model.Recommendation;
import com.facialanalyse.facialAPI.model.User;
import com.facialanalyse.facialAPI.model.Product;
import com.facialanalyse.facialAPI.repositories.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    @Autowired
    public RecommendationService(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    public List<Recommendation> getAllRecommendations() {
        return recommendationRepository.findAll();
    }

    public Optional<Recommendation> getRecommendationById(Long id) {
        return recommendationRepository.findById(id);
    }

    public Recommendation createRecommendation(Recommendation recommendation) {
        recommendation.setRecommendedAt(LocalDateTime.now());
        return recommendationRepository.save(recommendation);
    }

    public Optional<Recommendation> updateRecommendation(Long id, Recommendation recommendationDetails) {
        Optional<Recommendation> recommendation = recommendationRepository.findById(id);
        if (recommendation.isPresent()) {
            Recommendation existingRecommendation = recommendation.get();
            existingRecommendation.setUser(recommendationDetails.getUser());
            existingRecommendation.setProduct(recommendationDetails.getProduct());
            existingRecommendation.setRecommendedAt(LocalDateTime.now());
            return Optional.of(recommendationRepository.save(existingRecommendation));
        }
        return Optional.empty();
    }

    public boolean deleteRecommendation(Long id) {
        if (recommendationRepository.existsById(id)) {
            recommendationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Recommendation> getRecommendationsForUser(User user) {
        return recommendationRepository.findByUser(user);
    }

    public List<Recommendation> getRecommendationsForProduct(Product product) {
        return recommendationRepository.findByProduct(product);
    }

    public List<Recommendation> getRecommendationsByDateRange(LocalDateTime start, LocalDateTime end) {
        return recommendationRepository.findByRecommendedAtBetween(start, end);
    }

    public List<Recommendation> getRecommendationsForUserByDateRange(User user, LocalDateTime start, LocalDateTime end) {
        return recommendationRepository.findByUserAndRecommendedAtBetween(user, start, end);
    }

    public long countRecommendationsForProduct(Product product) {
        return recommendationRepository.countByProduct(product);
    }

    public List<Recommendation> getRecentRecommendationsForUser(User user, int limit) {
        List<Recommendation> recommendations = recommendationRepository.findByUser(user);
        recommendations.sort((r1, r2) -> r2.getRecommendedAt().compareTo(r1.getRecommendedAt()));
        return recommendations.subList(0, Math.min(limit, recommendations.size()));
    }
}