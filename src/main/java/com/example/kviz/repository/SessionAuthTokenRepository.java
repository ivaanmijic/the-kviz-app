package com.example.kviz.repository;

import com.example.kviz.database.PersistenceManager;
import com.example.kviz.model.SessionAuthToken;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

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
            SessionAuthToken tokenEntity = em.createQuery("""
                SELECT t FROM SessionAuthToken t
                JOIN FETCH t.admin a
                WHERE t.token = :token AND t.expiry > :now
                """, SessionAuthToken.class)
                    .setParameter("token", token)
                    .setParameter("now", LocalDateTime.now())
                    .getSingleResult();
            return Optional.of(tokenEntity);
        } catch (NoResultException e) {
            return Optional.empty();
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
