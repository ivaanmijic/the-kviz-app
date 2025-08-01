package com.example.kviz.service;

import com.example.kviz.model.Admin;
import com.example.kviz.model.SessionAuthToken;
import com.example.kviz.repository.SessionAuthTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class SessionAuthTokenService {

    private final SessionAuthTokenRepository sessionAuthTokenRepository;

    public SessionAuthTokenService() {
        this.sessionAuthTokenRepository = new SessionAuthTokenRepository();
    }

    public void createToken(String token, Admin admin, LocalDateTime expiry) {
        SessionAuthToken sessionAuthToken = new SessionAuthToken(token, admin, expiry);
        sessionAuthTokenRepository.save(sessionAuthToken);
    }

    public Optional<Admin> getAdminByToken(String token) {
        Optional<SessionAuthToken> tokenOptional = sessionAuthTokenRepository.findByToken(token);
        return tokenOptional.map(SessionAuthToken::getAdmin);
    }

    public void deleteToken(String token) {
        Optional<SessionAuthToken> tokenOptional = sessionAuthTokenRepository.findByToken(token);
        tokenOptional.ifPresent(sessionAuthTokenRepository::delete);
    }

}
