package com.example.kviz.repository;

import com.example.kviz.database.PersistenceManager;
import com.example.kviz.model.Admin;
import com.example.kviz.model.Quiz;
import com.example.kviz.model.dto.QuizDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class QuizRepository {

    public Quiz save(Quiz quiz) {
        EntityManager em = PersistenceManager.entityManager();

        try {
            em.getTransaction().begin();
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
        try (EntityManager em = PersistenceManager.entityManager()) {
            return Optional.ofNullable(em.find(Quiz.class, id));
        }
    }

    public List<Quiz> findByTitle(String title) {
        try (EntityManager em = PersistenceManager.entityManager()) {
            TypedQuery<Quiz> q = em.createQuery("SELECT q FROM Quiz q WHERE q.title = :title", Quiz.class);
            q.setParameter("title", title);
            return q.getResultList();
        }
    }

    public List<Quiz> findAll() {
        try (EntityManager em = PersistenceManager.entityManager()) {
            TypedQuery<Quiz> q = em.createQuery("SELECT q FROM Quiz q", Quiz.class);
            return q.getResultList();
        }
    }

    public List<Quiz> findAllPublic(Long selfId) {
        try (EntityManager em = PersistenceManager.entityManager()) {
            TypedQuery<Quiz> q = em.createQuery("SELECT q FROM Quiz q WHERE q.owner.id != :selfId AND q.visible", Quiz.class);
            q.setParameter("selfId", selfId);
            return q.getResultList();
        }
    }

    public List<Quiz> findAllByAdmin(Admin admin) {
        try (EntityManager em = PersistenceManager.entityManager()) {
            TypedQuery<Quiz> q = em.createQuery("SELECT q FROM Quiz q WHERE q.owner.id = :id", Quiz.class);
            q.setParameter("id", admin.getId());
            return q.getResultList();
        }
    }

    public List<Quiz> findAllByAdminId(Long id) {
        try (EntityManager em = PersistenceManager.entityManager()) {
            return em.createQuery("SELECT q FROM Quiz q WHERE q.owner.id = :id", Quiz.class)
                    .setParameter("id", id)
                    .getResultList();
        }
    }

    public void delete(Quiz quiz) {
        try (EntityManager em = PersistenceManager.entityManager()) {
            em.getTransaction().begin();
            Quiz managedQuiz = em.merge(quiz);
            em.remove(managedQuiz);
            em.getTransaction().commit();
        }
    }

    public void deleteById(Long id) {
        findById(id).ifPresent(this::delete);
    }
}
