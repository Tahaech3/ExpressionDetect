package com.facialanalyse.facialAPI.services;

import com.facialanalyse.facialAPI.model.Expression;
import com.facialanalyse.facialAPI.model.User;
import com.facialanalyse.facialAPI.repositories.ExpressionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.facialanalyse.facialAPI.service.ExpressionService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpressionServiceTest {

    @Mock
    private ExpressionRepository expressionRepository;

    @InjectMocks
    private ExpressionService expressionService;

    private User testUser;
    private Expression testExpression;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User(1L, "testUser", "test@example.com");
        testDateTime = LocalDateTime.now();
        testExpression = new Expression(1L, "Happy", testDateTime, testUser);
    }

    @Test
    void getAllExpressions() {
        List<Expression> expressions = Arrays.asList(
                testExpression,
                new Expression(2L, "Sad", testDateTime.plusHours(1), testUser)
        );
        when(expressionRepository.findAll()).thenReturn(expressions);

        List<Expression> result = expressionService.getAllExpressions();

        assertEquals(2, result.size());
        assertEquals("Happy", result.get(0).getType());
        assertEquals("Sad", result.get(1).getType());
    }

    @Test
    void getExpressionById() {
        when(expressionRepository.findById(1L)).thenReturn(Optional.of(testExpression));

        Optional<Expression> result = expressionService.getExpressionById(1L);

        assertTrue(result.isPresent());
        assertEquals("Happy", result.get().getType());
    }

    @Test
    void createExpression() {
        when(expressionRepository.save(any(Expression.class))).thenReturn(testExpression);

        Expression result = expressionService.createExpression(testExpression);

        assertEquals(1L, result.getId());
        assertEquals("Happy", result.getType());
    }

    @Test
    void updateExpression() {
        Expression updatedExpression = new Expression(1L, "Excited", testDateTime.plusHours(2), testUser);
        when(expressionRepository.findById(1L)).thenReturn(Optional.of(testExpression));
        when(expressionRepository.save(any(Expression.class))).thenReturn(updatedExpression);

        Optional<Expression> result = expressionService.updateExpression(1L, updatedExpression);

        assertTrue(result.isPresent());
        assertEquals("Excited", result.get().getType());
        assertEquals(testDateTime.plusHours(2), result.get().getDetectedAt());
    }

    @Test
    void deleteExpression() {
        when(expressionRepository.existsById(1L)).thenReturn(true);

        boolean result = expressionService.deleteExpression(1L);

        assertTrue(result);
        verify(expressionRepository, times(1)).deleteById(1L);
    }

    @Test
    void getExpressionsByUser() {
        List<Expression> expressions = Arrays.asList(
                testExpression,
                new Expression(2L, "Sad", testDateTime.plusHours(1), testUser)
        );
        when(expressionRepository.findByUser(testUser)).thenReturn(expressions);

        List<Expression> result = expressionService.getExpressionsByUser(testUser);

        assertEquals(2, result.size());
        assertEquals("Happy", result.get(0).getType());
        assertEquals("Sad", result.get(1).getType());
    }

    @Test
    void getExpressionsByUserAndTimeRange() {
        List<Expression> expressions = Arrays.asList(
                testExpression,
                new Expression(2L, "Sad", testDateTime.plusHours(1), testUser)
        );
        LocalDateTime start = testDateTime.minusHours(1);
        LocalDateTime end = testDateTime.plusHours(2);
        when(expressionRepository.findByUserAndDetectedAtBetween(testUser, start, end)).thenReturn(expressions);

        List<Expression> result = expressionService.getExpressionsByUserAndTimeRange(testUser, start, end);

        assertEquals(2, result.size());
        assertEquals("Happy", result.get(0).getType());
        assertEquals("Sad", result.get(1).getType());
    }

    @Test
    void getExpressionsByType() {
        List<Expression> expressions = Arrays.asList(
                testExpression,
                new Expression(2L, "Happy", testDateTime.plusHours(1), new User(2L, "anotherUser", "another@example.com"))
        );
        when(expressionRepository.findByType("Happy")).thenReturn(expressions);

        List<Expression> result = expressionService.getExpressionsByType("Happy");

        assertEquals(2, result.size());
        assertEquals("Happy", result.get(0).getType());
        assertEquals("Happy", result.get(1).getType());
    }

    @Test
    void countExpressionsByUserAndType() {
        when(expressionRepository.countByUserAndType(testUser, "Happy")).thenReturn(5L);

        long result = expressionService.countExpressionsByUserAndType(testUser, "Happy");

        assertEquals(5L, result);
    }
}