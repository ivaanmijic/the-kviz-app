package com.example.kviz.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.kviz.model.supporting.AdminRole;
import com.google.gson.annotations.Expose;

import jakarta.persistence.*;

@Entity
@Table(name = "admins", uniqueConstraints = {
        @UniqueConstraint(name = "UK_admin_email", columnNames = "email"),
        @UniqueConstraint(name = "UK_admin_username", columnNames = "username")
})
public class Admin {
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Expose
    @Column(nullable = false)
    private String email;

    @Expose
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Expose
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminRole role;

    @Expose
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Expose
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Quiz> quizzes =  new ArrayList<>();

    @OneToMany(
            mappedBy = "admin",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<SessionAuthToken> sessionAuthTokens = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // MARK: - Initialization

    public Admin() {
    }

    public Admin(String email, String username, String password, AdminRole role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // MARK: - Getters and Setters

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public AdminRole getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public List<SessionAuthToken> getSessionAuthTokens() {
        return sessionAuthTokens;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(AdminRole role) {
        this.role = role;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public void setSessionAuthTokens(List<SessionAuthToken> sessionAuthTokens) {
        this.sessionAuthTokens = sessionAuthTokens;
    }
}
