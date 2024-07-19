package com.facialanalyse.facialAPI.controllers;

import com.facialanalyse.facialAPI.model.User;
import com.facialanalyse.facialAPI.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.facialanalyse.facialAPI.controller.UserController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setUsername("testuser");
    }

    @Test
    void getAllUsers() {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(testUser));

        List<User> users = userController.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(testUser, users.get(0));
        verify(userService).getAllUsers();
    }

    @Test
    void getUserById() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
        verify(userService).getUserById(1L);
    }

    @Test
    void getUserById_NotFound() {
        when(userService.getUserById(2L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).getUserById(2L);
    }

    @Test
    void createUser() {
        when(userService.createUser(any(User.class))).thenReturn(testUser);

        User createdUser = userController.createUser(testUser);

        assertEquals(testUser, createdUser);
        verify(userService).createUser(testUser);
    }

    @Test
    void updateUser() {
        User updatedUser = new User();
        updatedUser.setName("Updated User");
        updatedUser.setEmail("updated@example.com");

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(Optional.of(updatedUser));

        ResponseEntity<User> response = userController.updateUser(1L, updatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(userService).updateUser(eq(1L), any(User.class));
    }

    @Test
    void updateUser_NotFound() {
        User updatedUser = new User();
        when(userService.updateUser(eq(2L), any(User.class))).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.updateUser(2L, updatedUser);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).updateUser(eq(2L), any(User.class));
    }

    @Test
    void deleteUser() {
        when(userService.deleteUser(1L)).thenReturn(true);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).deleteUser(1L);
    }

    @Test
    void deleteUser_NotFound() {
        when(userService.deleteUser(2L)).thenReturn(false);

        ResponseEntity<Void> response = userController.deleteUser(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService).deleteUser(2L);
    }
}