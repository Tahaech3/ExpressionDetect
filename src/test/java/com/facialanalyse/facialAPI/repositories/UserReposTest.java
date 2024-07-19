package com.facialanalyse.facialAPI.repositories;

import com.facialanalyse.facialAPI.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserReposTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByUsername() {
        User user = new User();
        user.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> result = userRepository.findByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userRepository.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    public void testExistsByEmail() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        boolean result = userRepository.existsByEmail("test@example.com");

        assertTrue(result);
        verify(userRepository).existsByEmail("test@example.com");
    }

    @Test
    public void testExistsByUsername() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        boolean result = userRepository.existsByUsername("testuser");

        assertTrue(result);
        verify(userRepository).existsByUsername("testuser");
    }

    @Test
    public void testFindByNameContainingIgnoreCase() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findByNameContainingIgnoreCase("John")).thenReturn(users);

        List<User> result = userRepository.findByNameContainingIgnoreCase("John");

        assertEquals(2, result.size());
        verify(userRepository).findByNameContainingIgnoreCase("John");
    }

    @Test
    public void testCountExpressionsByUserId() {
        when(userRepository.countExpressionsByUserId(1L)).thenReturn(5L);

        long result = userRepository.countExpressionsByUserId(1L);

        assertEquals(5L, result);
        verify(userRepository).countExpressionsByUserId(1L);
    }

    @Test
    public void testFindByRole() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findByRole("ADMIN")).thenReturn(users);

        List<User> result = userRepository.findByRole("ADMIN");

        assertEquals(2, result.size());
        verify(userRepository).findByRole("ADMIN");
    }

    @Test
    public void testFindByEnabledTrue() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findByEnabledTrue()).thenReturn(users);

        List<User> result = userRepository.findByEnabledTrue();

        assertEquals(2, result.size());
        verify(userRepository).findByEnabledTrue();
    }

    @Test
    public void testFindByAccountNonExpiredFalse() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findByAccountNonExpiredFalse()).thenReturn(users);

        List<User> result = userRepository.findByAccountNonExpiredFalse();

        assertEquals(2, result.size());
        verify(userRepository).findByAccountNonExpiredFalse();
    }
}