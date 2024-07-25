package com.facialanalyse.facialAPI.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.facialanalyse.facialAPI.model.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpressionModelTest {

    @Mock
    private User mockUser;

    @Mock
    private Expression mockExpression;

    private Expression expression;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testDateTime = LocalDateTime.now();
        expression = new Expression(1L, "smile", testDateTime, mockUser);
    }

    @Test
    void testExpressionCreation() {
        assertNotNull(expression);
        assertEquals(1L, expression.getId());
        assertEquals("smile", expression.getType());
        assertEquals(testDateTime, expression.getDetectedAt());
        assertEquals(mockUser, expression.getUser());
    }

    @Test
    void testSetAndGetId() {
        expression.setId(2L);
        assertEquals(2L, expression.getId());
    }

    @Test
    void testSetAndGetType() {
        expression.setType("frown");
        assertEquals("frown", expression.getType());
    }

    @Test
    void testSetAndGetDetectedAt() {
        LocalDateTime newDateTime = LocalDateTime.now().plusHours(1);
        expression.setDetectedAt(newDateTime);
        assertEquals(newDateTime, expression.getDetectedAt());
    }

    @Test
    void testSetAndGetUser() {
        User newUser = mock(User.class);
        expression.setUser(newUser);
        assertEquals(newUser, expression.getUser());
    }

    @Test
    void testNullableFields() {
        Expression nullExpression = new Expression();
        assertNull(nullExpression.getId());
        assertNull(nullExpression.getType());
        assertNull(nullExpression.getDetectedAt());
        assertNull(nullExpression.getUser());
    }

    @Test
    void testMockExpression() {
        when(mockExpression.getId()).thenReturn(3L);
        when(mockExpression.getType()).thenReturn("surprise");
        when(mockExpression.getDetectedAt()).thenReturn(testDateTime);
        when(mockExpression.getUser()).thenReturn(mockUser);

        assertEquals(3L, mockExpression.getId());
        assertEquals("surprise", mockExpression.getType());
        assertEquals(testDateTime, mockExpression.getDetectedAt());
        assertEquals(mockUser, mockExpression.getUser());

        verify(mockExpression, times(1)).getId();
        verify(mockExpression, times(1)).getType();
        verify(mockExpression, times(1)).getDetectedAt();
        verify(mockExpression, times(1)).getUser();
    }

    @Test
    void testUserAssociation() {
        when(mockUser.getId()).thenReturn(1L);

        Expression expressionWithUser = new Expression(1L, "smile", testDateTime, mockUser);
        assertEquals(mockUser, expressionWithUser.getUser());
        assertEquals(1L, expressionWithUser.getUser().getId());

        verify(mockUser, times(1)).getId();
    }
}