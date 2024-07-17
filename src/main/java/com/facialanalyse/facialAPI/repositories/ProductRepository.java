package com.facialanalyse.facialAPI.repositories;

import com.facialanalyse.facialAPI.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Trouver les produits par nom
    List<Product> findByNameContainingIgnoreCase(String name);

    // Trouver les produits par description
    List<Product> findByDescriptionContainingIgnoreCase(String description);

    // Trouver les produits dans une fourchette de prix
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    // Trouver les produits par nom et trier par prix
    List<Product> findByNameContainingIgnoreCaseOrderByPriceAsc(String name);
}