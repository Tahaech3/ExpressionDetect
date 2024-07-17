package com.facialanalyse.facialAPI.service;

import com.facialanalyse.facialAPI.model.User;
import com.facialanalyse.facialAPI.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(userDetails.getName());
                    existingUser.setEmail(userDetails.getEmail());
                    existingUser.setUsername(userDetails.getUsername());
                    existingUser.setRoles(userDetails.getRoles());
                    existingUser.setAccountNonExpired(userDetails.isAccountNonExpired());
                    existingUser.setAccountNonLocked(userDetails.isAccountNonLocked());
                    existingUser.setCredentialsNonExpired(userDetails.isCredentialsNonExpired());
                    existingUser.setEnabled(userDetails.isEnabled());
                    return userRepository.save(existingUser);
                });
    }

    public boolean deleteUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                })
                .orElse(false);
    }

    public boolean addRoleToUser(Long userId, String role) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.getRoles().add(role);
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }

    public boolean removeRoleFromUser(Long userId, String role) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.getRoles().remove(role);
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }

    public boolean updateUserStatus(Long userId, boolean enabled) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setEnabled(enabled);
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }
}
