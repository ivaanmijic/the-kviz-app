package com.example.kviz.repository;

import com.example.kviz.database.PersistenceManager;
import com.example.kviz.model.Admin;
import com.example.kviz.model.Quiz;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class QuizRepository {

    public Quiz save(Quiz quiz) {
        EntityManager em = PersistenceManager.createEntityManager();

        try {
            em.getTransaction();
            if (quiz.getId() == null) {
                em.persist(quiz);
            } else {
                quiz = em.merge(quiz);
            }
            em.getTransaction().commit();
            return quiz;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<Quiz> findById(Long id) {
        try (EntityManager em = PersistenceManager.createEntityManager()) {
            return Optional.ofNullable(em.find(Quiz.class, id));
        }
    }

    public List<Quiz> findByTitle(String title) {
        try (EntityManager em = PersistenceManager.createEntityManager()) {
            TypedQuery<Quiz> q = em.createQuery("SELECT q FROM Quiz q WHERE q.title = :title", Quiz.class);
            q.setParameter("title", title);
            return q.getResultList();
        }
    }

    public List<Quiz> findAll() {
        try (EntityManager em = PersistenceManager.createEntityManager()) {
            TypedQuery<Quiz> q = em.createQuery("SELECT q FROM Quiz q", Quiz.class);
            return q.getResultList();
        }
    }

    public List<Quiz> findAllByAdmin(Admin admin) {
        try (EntityManager em = PersistenceManager.createEntityManager()) {
            TypedQuery<Quiz> q = em.createQuery("SELECT q FROM Quiz q WHERE q.owner.id = :id", Quiz.class);
            q.setParameter("id", admin.getId());
            return q.getResultList();
        }
    }

    public void delete(Quiz quiz) {
        try (EntityManager em = PersistenceManager.createEntityManager()) {
            em.getTransaction().begin();
            em.remove(quiz);
            em.getTransaction().commit();
        }
    }
}
