package com.example.kviz.service;

import com.example.kviz.model.Admin;
import com.example.kviz.model.SessionAuthToken;
import com.example.kviz.repository.SessionAuthTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;

public class SessionAuthTokenService {

    private static final Logger log = LoggerFactory.getLogger(SessionAuthTokenService.class);
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

    public void updateToken(String token, Admin admin) {
        Optional<SessionAuthToken> tokenOptional = sessionAuthTokenRepository.findByToken(token);
        if (tokenOptional.isPresent()) {
            SessionAuthToken sessionAuthToken = tokenOptional.get();
            sessionAuthToken.setAdmin(admin);
            sessionAuthTokenRepository.save(sessionAuthToken);
        }
    }

    public void deleteToken(String token) {
        Optional<SessionAuthToken> tokenOptional = sessionAuthTokenRepository.findByToken(token);
        tokenOptional.ifPresent(sessionAuthTokenRepository::delete);
    }

}
