package com.example.kviz.repository;

import com.example.kviz.database.PersistenceManager;
import com.example.kviz.model.SessionAuthToken;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

public class SessionAuthTokenRepository {

    public SessionAuthToken save(SessionAuthToken sessionAuthToken) {
        EntityManager em = PersistenceManager.entityManager();

        try {
            em.getTransaction().begin();
            if (sessionAuthToken.getToken() == null) {
                em.persist(sessionAuthToken);
            } else {
                sessionAuthToken = em.merge(sessionAuthToken);
            }
            em.getTransaction().commit();
            return sessionAuthToken;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<SessionAuthToken> findByToken(String token) {
        try (EntityManager em = PersistenceManager.entityManager()) {
            SessionAuthToken authToken = em.find(SessionAuthToken.class, token);
            if (authToken == null || authToken.getExpiry().isBefore(LocalDateTime.now())) {
                return Optional.empty();
            }
            return Optional.of(authToken);
        }
    }

    public void delete(SessionAuthToken sessionAuthToken) {
        EntityManager em = PersistenceManager.entityManager();
        try {
            em.getTransaction().begin();
            SessionAuthToken menagedToken = em.contains(sessionAuthToken) ? sessionAuthToken : em.merge(sessionAuthToken);
            em.remove(menagedToken);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

}
