package com.facialanalyse.facialAPI.models;

import com.facialanalyse.facialAPI.model.Expression;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import com.facialanalyse.facialAPI.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {

    @Test
    void testDefaultConstructor() {
        User user = new User();
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getExpressions());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNotNull(user.getRoles());
        assertTrue(user.getRoles().isEmpty());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void testFullConstructor() {
        Long id = 1L;
        String name = "John Doe";
        String email = "john@example.com";
        Set<Expression> expressions = new HashSet<>();
        String username = "johndoe";
        String password = "password123";
        Set<String> roles = new HashSet<>();
        roles.add("USER");

        User user = new User(id, name, email, expressions, username, password, roles, true, true, true, true);

        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(expressions, user.getExpressions());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(roles, user.getRoles());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void testPartialConstructor() {
        Long id = 1L;
        String name = "John Doe";
        String email = "john@example.com";

        User user = new User(id, name, email);

        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertNull(user.getExpressions());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNotNull(user.getRoles());
        assertTrue(user.getRoles().isEmpty());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();

        Long id = 1L;
        user.setId(id);
        assertEquals(id, user.getId());

        String name = "Jane Doe";
        user.setName(name);
        assertEquals(name, user.getName());

        String email = "jane@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());

        Set<Expression> expressions = new HashSet<>();
        Expression expression = new Expression();
        expressions.add(expression);
        user.setExpressions(expressions);
        assertEquals(expressions, user.getExpressions());

        String username = "janedoe";
        user.setUsername(username);
        assertEquals(username, user.getUsername());

        String password = "password456";
        user.setPassword(password);
        assertEquals(password, user.getPassword());

        Set<String> roles = new HashSet<>();
        roles.add("ADMIN");
        user.setRoles(roles);
        assertEquals(roles, user.getRoles());

        user.setAccountNonExpired(false);
        assertFalse(user.isAccountNonExpired());

        user.setAccountNonLocked(false);
        assertFalse(user.isAccountNonLocked());

        user.setCredentialsNonExpired(false);
        assertFalse(user.isCredentialsNonExpired());

        user.setEnabled(false);
        assertFalse(user.isEnabled());
    }

    @Test
    void testRolesManipulation() {
        User user = new User();

        assertTrue(user.getRoles().isEmpty());

        user.getRoles().add("USER");
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().contains("USER"));

        user.getRoles().add("ADMIN");
        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().contains("ADMIN"));

        user.getRoles().remove("USER");
        assertEquals(1, user.getRoles().size());
        assertFalse(user.getRoles().contains("USER"));
        assertTrue(user.getRoles().contains("ADMIN"));
    }
}