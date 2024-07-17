package com.facialanalyse.facialAPI.repositories;

import com.facialanalyse.facialAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Trouver un utilisateur par son nom d'utilisateur
    Optional<User> findByUsername(String username);

    // Trouver un utilisateur par son email
    Optional<User> findByEmail(String email);

    // Vérifier si un email existe déjà
    boolean existsByEmail(String email);

    // Vérifier si un nom d'utilisateur existe déjà
    boolean existsByUsername(String username);

    // Trouver des utilisateurs par leur nom
    List<User> findByNameContainingIgnoreCase(String name);

    // Compter le nombre d'expressions pour un utilisateur
    @Query("SELECT COUNT(e) FROM User u JOIN u.expressions e WHERE u.id = :userId")
    long countExpressionsByUserId(@Param("userId") Long userId);

    // Trouver des utilisateurs par rôle
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    List<User> findByRole(@Param("role") String role);

    // Trouver des utilisateurs actifs
    List<User> findByEnabledTrue();

    // Trouver des utilisateurs dont le compte a expiré
    List<User> findByAccountNonExpiredFalse();
}