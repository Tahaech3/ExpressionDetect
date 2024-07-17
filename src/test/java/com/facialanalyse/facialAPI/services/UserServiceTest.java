package com.facialanalyse.facialAPI.services;

import com.facialanalyse.facialAPI.model.User;
import com.facialanalyse.facialAPI.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.facialanalyse.facialAPI.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setUsername("testuser");
        testUser.setRoles(new HashSet<>(Arrays.asList("USER")));
        testUser.setAccountNonExpired(true);
        testUser.setAccountNonLocked(true);
        testUser.setCredentialsNonExpired(true);
        testUser.setEnabled(true);
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(testUser, users.get(0));
        verify(userRepository).findAll();
    }

    @Test
    void getUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> user = userService.getUserById(1L);

        assertTrue(user.isPresent());
        assertEquals(testUser, user.get());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserByUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        Optional<User> user = userService.getUserByUsername("testuser");

        assertTrue(user.isPresent());
        assertEquals(testUser, user.get());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User createdUser = userService.createUser(testUser);

        assertEquals(testUser, createdUser);
        verify(userRepository).save(testUser);
    }

    @Test
    void updateUser() {
        User updatedUser = new User();
        updatedUser.setName("Updated User");
        updatedUser.setEmail("updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        Optional<User> result = userService.updateUser(1L, updatedUser);

        assertTrue(result.isPresent());
        assertEquals("Updated User", result.get().getName());
        assertEquals("updated@example.com", result.get().getEmail());
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        boolean result = userService.deleteUser(1L);

        assertTrue(result);
        verify(userRepository).findById(1L);
        verify(userRepository).delete(testUser);
    }

    @Test
    void addRoleToUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        boolean result = userService.addRoleToUser(1L, "ADMIN");

        assertTrue(result);
        assertTrue(testUser.getRoles().contains("ADMIN"));
        verify(userRepository).findById(1L);
        verify(userRepository).save(testUser);
    }

    @Test
    void removeRoleFromUser() {
        testUser.getRoles().add("ADMIN");
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        boolean result = userService.removeRoleFromUser(1L, "ADMIN");

        assertTrue(result);
        assertFalse(testUser.getRoles().contains("ADMIN"));
        verify(userRepository).findById(1L);
        verify(userRepository).save(testUser);
    }

    @Test
    void updateUserStatus() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        boolean result = userService.updateUserStatus(1L, false);

        assertTrue(result);
        assertFalse(testUser.isEnabled());
        verify(userRepository).findById(1L);
        verify(userRepository).save(testUser);
    }
}