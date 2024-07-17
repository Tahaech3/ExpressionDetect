package com.facialanalyse.facialAPI.controller;

import com.facialanalyse.facialAPI.model.Expression;
import com.facialanalyse.facialAPI.service.ExpressionService;
import com.facialanalyse.facialAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/expressions")
public class ExpressionController {

    private final ExpressionService expressionService;
    private final UserService userService;

    @Autowired
    public ExpressionController(ExpressionService expressionService, UserService userService) {
        this.expressionService = expressionService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Expression>> getAllExpressions() {
        return ResponseEntity.ok(expressionService.getAllExpressions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expression> getExpressionById(@PathVariable Long id) {
        return expressionService.getExpressionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Expression> createExpression(@RequestBody Expression expression) {
        return ResponseEntity.status(HttpStatus.CREATED).body(expressionService.createExpression(expression));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expression> updateExpression(@PathVariable Long id, @RequestBody Expression expressionDetails) {
        return expressionService.updateExpression(id, expressionDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpression(@PathVariable Long id) {
        return expressionService.deleteExpression(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Expression>> getExpressionsByUser(@PathVariable Long userId) {
        return userService.getUserById(userId)
                .map(user -> ResponseEntity.ok(expressionService.getExpressionsByUser(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}/dateRange")
    public ResponseEntity<List<Expression>> getExpressionsByUserAndTimeRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return userService.getUserById(userId)
                .map(user -> ResponseEntity.ok(expressionService.getExpressionsByUserAndTimeRange(user, start, end)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Expression>> getExpressionsByType(@PathVariable String type) {
        return ResponseEntity.ok(expressionService.getExpressionsByType(type));
    }

    @GetMapping("/user/{userId}/type/{type}/count")
    public ResponseEntity<Long> countExpressionsByUserAndType(@PathVariable Long userId, @PathVariable String type) {
        return userService.getUserById(userId)
                .map(user -> ResponseEntity.ok(expressionService.countExpressionsByUserAndType(user, type)))
                .orElse(ResponseEntity.notFound().build());
    }
}