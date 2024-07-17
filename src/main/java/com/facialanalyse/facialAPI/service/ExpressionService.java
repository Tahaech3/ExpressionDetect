package com.facialanalyse.facialAPI.service;

import com.facialanalyse.facialAPI.model.Expression;
import com.facialanalyse.facialAPI.model.User;
import com.facialanalyse.facialAPI.repositories.ExpressionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExpressionService {

    private final ExpressionRepository expressionRepository;

    @Autowired
    public ExpressionService(ExpressionRepository expressionRepository) {
        this.expressionRepository = expressionRepository;
    }

    public List<Expression> getAllExpressions() {
        return expressionRepository.findAll();
    }

    public Optional<Expression> getExpressionById(Long id) {
        return expressionRepository.findById(id);
    }

    public Expression createExpression(Expression expression) {
        return expressionRepository.save(expression);
    }

    public Optional<Expression> updateExpression(Long id, Expression expressionDetails) {
        Optional<Expression> expression = expressionRepository.findById(id);
        if (expression.isPresent()) {
            Expression existingExpression = expression.get();
            existingExpression.setType(expressionDetails.getType());
            existingExpression.setDetectedAt(expressionDetails.getDetectedAt());
            existingExpression.setUser(expressionDetails.getUser());
            return Optional.of(expressionRepository.save(existingExpression));
        }
        return Optional.empty();
    }

    public boolean deleteExpression(Long id) {
        if (expressionRepository.existsById(id)) {
            expressionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Expression> getExpressionsByUser(User user) {
        return expressionRepository.findByUser(user);
    }

    public List<Expression> getExpressionsByUserAndTimeRange(User user, LocalDateTime start, LocalDateTime end) {
        return expressionRepository.findByUserAndDetectedAtBetween(user, start, end);
    }

    public List<Expression> getExpressionsByType(String type) {
        return expressionRepository.findByType(type);
    }

    public long countExpressionsByUserAndType(User user, String type) {
        return expressionRepository.countByUserAndType(user, type);
    }
}