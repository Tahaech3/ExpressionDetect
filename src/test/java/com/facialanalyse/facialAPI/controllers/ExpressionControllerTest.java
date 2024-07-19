package com.facialanalyse.facialAPI.controllers;

import com.facialanalyse.facialAPI.model.Expression;
import com.facialanalyse.facialAPI.model.User;
import com.facialanalyse.facialAPI.service.ExpressionService;
import com.facialanalyse.facialAPI.service.UserService;
import com.facialanalyse.facialAPI.controller.ExpressionController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpressionControllerTest {

    @Mock
    private ExpressionService expressionService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ExpressionController expressionController;

    private Expression testExpression;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");

        testExpression = new Expression();
        testExpression.setId(1L);
        testExpression.setType("Happy");
        testExpression.setUser(testUser);
        testExpression.setDetectedAt(LocalDateTime.now());
    }

    @Test
    void getAllExpressions() {
        when(expressionService.getAllExpressions()).thenReturn(Arrays.asList(testExpression));

        ResponseEntity<List<Expression>> response = expressionController.getAllExpressions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testExpression, response.getBody().get(0));
        verify(expressionService).getAllExpressions();
    }

    @Test
    void getExpressionById() {
        when(expressionService.getExpressionById(1L)).thenReturn(Optional.of(testExpression));

        ResponseEntity<Expression> response = expressionController.getExpressionById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testExpression, response.getBody());
        verify(expressionService).getExpressionById(1L);
    }

    @Test
    void getExpressionById_NotFound() {
        when(expressionService.getExpressionById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Expression> response = expressionController.getExpressionById(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(expressionService).getExpressionById(2L);
    }

    @Test
    void createExpression() {
        when(expressionService.createExpression(any(Expression.class))).thenReturn(testExpression);

        ResponseEntity<Expression> response = expressionController.createExpression(testExpression);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testExpression, response.getBody());
        verify(expressionService).createExpression(testExpression);
    }

    @Test
    void updateExpression() {
        Expression updatedExpression = new Expression();
        updatedExpression.setType("Sad");

        when(expressionService.updateExpression(eq(1L), any(Expression.class))).thenReturn(Optional.of(updatedExpression));

        ResponseEntity<Expression> response = expressionController.updateExpression(1L, updatedExpression);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedExpression, response.getBody());
        verify(expressionService).updateExpression(eq(1L), any(Expression.class));
    }

    @Test
    void updateExpression_NotFound() {
        Expression updatedExpression = new Expression();
        when(expressionService.updateExpression(eq(2L), any(Expression.class))).thenReturn(Optional.empty());

        ResponseEntity<Expression> response = expressionController.updateExpression(2L, updatedExpression);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(expressionService).updateExpression(eq(2L), any(Expression.class));
    }

    @Test
    void deleteExpression() {
        when(expressionService.deleteExpression(1L)).thenReturn(true);

        ResponseEntity<Void> response = expressionController.deleteExpression(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(expressionService).deleteExpression(1L);
    }

    @Test
    void deleteExpression_NotFound() {
        when(expressionService.deleteExpression(2L)).thenReturn(false);

        ResponseEntity<Void> response = expressionController.deleteExpression(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(expressionService).deleteExpression(2L);
    }

    @Test
    void getExpressionsByUser() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));
        when(expressionService.getExpressionsByUser(testUser)).thenReturn(Arrays.asList(testExpression));

        ResponseEntity<List<Expression>> response = expressionController.getExpressionsByUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testExpression, response.getBody().get(0));
        verify(userService).getUserById(1L);
        verify(expressionService).getExpressionsByUser(testUser);
    }

    @Test
    void getExpressionsByUser_UserNotFound() {
        when(userService.getUserById(2L)).thenReturn(Optional.empty());

        ResponseEntity<List<Expression>> response = expressionController.getExpressionsByUser(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).getUserById(2L);
    }

    @Test
    void getExpressionsByUserAndTimeRange() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();

        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));
        when(expressionService.getExpressionsByUserAndTimeRange(testUser, start, end)).thenReturn(Arrays.asList(testExpression));

        ResponseEntity<List<Expression>> response = expressionController.getExpressionsByUserAndTimeRange(1L, start, end);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testExpression, response.getBody().get(0));
        verify(userService).getUserById(1L);
        verify(expressionService).getExpressionsByUserAndTimeRange(testUser, start, end);
    }

    @Test
    void getExpressionsByUserAndTimeRange_UserNotFound() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();

        when(userService.getUserById(2L)).thenReturn(Optional.empty());

        ResponseEntity<List<Expression>> response = expressionController.getExpressionsByUserAndTimeRange(2L, start, end);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).getUserById(2L);
    }

    @Test
    void getExpressionsByType() {
        when(expressionService.getExpressionsByType("Happy")).thenReturn(Arrays.asList(testExpression));

        ResponseEntity<List<Expression>> response = expressionController.getExpressionsByType("Happy");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testExpression, response.getBody().get(0));
        verify(expressionService).getExpressionsByType("Happy");
    }

    @Test
    void countExpressionsByUserAndType() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));
        when(expressionService.countExpressionsByUserAndType(testUser, "Happy")).thenReturn(5L);

        ResponseEntity<Long> response = expressionController.countExpressionsByUserAndType(1L, "Happy");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody());
        verify(userService).getUserById(1L);
        verify(expressionService).countExpressionsByUserAndType(testUser, "Happy");
    }

    @Test
    void countExpressionsByUserAndType_UserNotFound() {
        when(userService.getUserById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Long> response = expressionController.countExpressionsByUserAndType(2L, "Happy");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).getUserById(2L);
    }
}