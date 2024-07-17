package com.facialanalyse.facialAPI.controller;

import com.facialanalyse.facialAPI.model.Product;
import com.facialanalyse.facialAPI.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productService.updateProduct(id, productDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String description) {
        if (name != null && !name.isEmpty()) {
            return ResponseEntity.ok(productService.searchProductsByName(name));
        } else if (description != null && !description.isEmpty()) {
            return ResponseEntity.ok(productService.searchProductsByDescription(description));
        } else {
            return ResponseEntity.ok(productService.getAllProducts());
        }
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsInPriceRange(@RequestParam Double minPrice,
                                                                 @RequestParam Double maxPrice) {
        return ResponseEntity.ok(productService.getProductsInPriceRange(minPrice, maxPrice));
    }

    @GetMapping("/search-sorted")
    public ResponseEntity<List<Product>> searchProductsSortedByPrice(@RequestParam String name) {
        return ResponseEntity.ok(productService.searchProductsByNameSortedByPrice(name));
    }
}