package com.example.kviz.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "session_tokens")
public class SessionAuthToken {

    @Id
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @Column(name = "expiry", nullable = false)
    private LocalDateTime expiry;

    public SessionAuthToken(String token, Admin admin, LocalDateTime expiry) {
        this.token = token;
        this.admin = admin;
        this.expiry = expiry;
    }

    public SessionAuthToken() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDateTime expiry) {
        this.expiry = expiry;
    }
}
