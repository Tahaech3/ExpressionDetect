package com.facialanalyse.facialAPI.repositories;

import com.facialanalyse.facialAPI.model.Recommendation;
import com.facialanalyse.facialAPI.model.User;
import com.facialanalyse.facialAPI.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    // Trouver toutes les recommandations pour un utilisateur
    List<Recommendation> findByUser(User user);

    // Trouver toutes les recommandations pour un produit
    List<Recommendation> findByProduct(Product product);

    // Trouver les recommandations dans un intervalle de temps
    List<Recommendation> findByRecommendedAtBetween(LocalDateTime start, LocalDateTime end);

    // Trouver les recommandations pour un utilisateur dans un intervalle de temps
    List<Recommendation> findByUserAndRecommendedAtBetween(User user, LocalDateTime start, LocalDateTime end);

    // Compter le nombre de recommandations pour un produit
    long countByProduct(Product product);
}