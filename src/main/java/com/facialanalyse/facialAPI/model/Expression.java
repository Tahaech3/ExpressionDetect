package com.facialanalyse.facialAPI.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "expressions")
public class Expression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // e.g., smile, frown, etc.

    @Column(nullable = false)
    private LocalDateTime detectedAt;

    // Many-to-one relationship with the User entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Constructeur par défaut
    public Expression(){

    }
    //Constructeur avec paramètres
    public Expression(Long id, String type, LocalDateTime detectedAt, User user) {
        this.id = id;
        this.type = type;
        this.detectedAt = detectedAt;
        this.user = user;
    }
// Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDetectedAt() {
        return detectedAt;
    }

    public void setDetectedAt(LocalDateTime detectedAt) {
        this.detectedAt = detectedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
