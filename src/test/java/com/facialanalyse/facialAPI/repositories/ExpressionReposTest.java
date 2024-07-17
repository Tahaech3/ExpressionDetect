package com.facialanalyse.facialAPI.repositories;

import com.facialanalyse.facialAPI.model.Expression;
import com.facialanalyse.facialAPI.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ExpressionReposTest {

    @Mock
    private ExpressionRepository expressionRepository;

    private User testUser;
    private Expression testExpression1;
    private Expression testExpression2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);

        testExpression1 = new Expression();
        testExpression1.setId(1L);
        testExpression1.setType("smile");
        testExpression1.setDetectedAt(LocalDateTime.now());
        testExpression1.setUser(testUser);

        testExpression2 = new Expression();
        testExpression2.setId(2L);
        testExpression2.setType("frown");
        testExpression2.setDetectedAt(LocalDateTime.now().plusHours(1));
        testExpression2.setUser(testUser);
    }

    @Test
    public void testFindByUser() {
        List<Expression> expressions = Arrays.asList(testExpression1, testExpression2);
        when(expressionRepository.findByUser(testUser)).thenReturn(expressions);

        List<Expression> result = expressionRepository.findByUser(testUser);

        assertEquals(2, result.size());
        verify(expressionRepository).findByUser(testUser);
    }

    @Test
    public void testFindByUserAndDetectedAtBetween() {
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);
        List<Expression> expressions = Arrays.asList(testExpression1, testExpression2);
        when(expressionRepository.findByUserAndDetectedAtBetween(testUser, start, end)).thenReturn(expressions);

        List<Expression> result = expressionRepository.findByUserAndDetectedAtBetween(testUser, start, end);

        assertEquals(2, result.size());
        verify(expressionRepository).findByUserAndDetectedAtBetween(testUser, start, end);
    }

    @Test
    public void testFindByType() {
        List<Expression> expressions = Arrays.asList(testExpression1);
        when(expressionRepository.findByType("smile")).thenReturn(expressions);

        List<Expression> result = expressionRepository.findByType("smile");

        assertEquals(1, result.size());
        assertEquals("smile", result.get(0).getType());
        verify(expressionRepository).findByType("smile");
    }

    @Test
    public void testCountByUserAndType() {
        when(expressionRepository.countByUserAndType(testUser, "smile")).thenReturn(1L);

        long count = expressionRepository.countByUserAndType(testUser, "smile");

        assertEquals(1L, count);
        verify(expressionRepository).countByUserAndType(testUser, "smile");
    }
}