package com.facialanalyse.facialAPI.controller;

import com.facialanalyse.facialAPI.model.Recommendation;
import com.facialanalyse.facialAPI.model.User;
import com.facialanalyse.facialAPI.model.Product;
import com.facialanalyse.facialAPI.service.RecommendationService;
import com.facialanalyse.facialAPI.service.UserService;
import com.facialanalyse.facialAPI.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.facialanalyse.facialAPI.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService, UserService userService, ProductService productService) {
        this.recommendationService = recommendationService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Recommendation>> getAllRecommendations() {
        return ResponseEntity.ok(recommendationService.getAllRecommendations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recommendation> getRecommendationById(@PathVariable Long id) {
        return recommendationService.getRecommendationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Recommendation> createRecommendation(@RequestBody Recommendation recommendation) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recommendationService.createRecommendation(recommendation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recommendation> updateRecommendation(@PathVariable Long id, @RequestBody Recommendation recommendationDetails) {
        return recommendationService.updateRecommendation(id, recommendationDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecommendation(@PathVariable Long id) {
        return recommendationService.deleteRecommendation(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getRecommendationsForUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return ResponseEntity.ok(recommendationService.getRecommendationsForUser(user));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Recommendation>> getRecommendationsForProduct(@PathVariable Long productId) {
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        return ResponseEntity.ok(recommendationService.getRecommendationsForProduct(product));
    }

    @GetMapping("/dateRange")
    public ResponseEntity<List<Recommendation>> getRecommendationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(recommendationService.getRecommendationsByDateRange(start, end));
    }

    @GetMapping("/user/{userId}/dateRange")
    public ResponseEntity<List<Recommendation>> getRecommendationsForUserByDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return ResponseEntity.ok(recommendationService.getRecommendationsForUserByDateRange(user, start, end));
    }

    @GetMapping("/product/{productId}/count")
    public ResponseEntity<Long> countRecommendationsForProduct(@PathVariable Long productId) {
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        return ResponseEntity.ok(recommendationService.countRecommendationsForProduct(product));
    }

    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<List<Recommendation>> getRecentRecommendationsForUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "5") int limit) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return ResponseEntity.ok(recommendationService.getRecentRecommendationsForUser(user, limit));
    }
}