package com.facialanalyse.facialAPI.repositories;

import com.facialanalyse.facialAPI.model.Expression;
import com.facialanalyse.facialAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpressionRepository extends JpaRepository<Expression, Long> {

    // Trouver toutes les expressions d'un utilisateur
    List<Expression> findByUser(User user);

    // Trouver les expressions d'un utilisateur dans un intervalle de temps
    List<Expression> findByUserAndDetectedAtBetween(User user, LocalDateTime start, LocalDateTime end);

    // Trouver les expressions par type
    List<Expression> findByType(String type);

    // Compter le nombre d'expressions d'un certain type pour un utilisateur
    long countByUserAndType(User user, String type);
}