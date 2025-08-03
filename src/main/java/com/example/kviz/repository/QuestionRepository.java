package com.example.kviz.repository;

import com.example.kviz.database.PersistenceManager;
import com.example.kviz.model.Quiz;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import com.example.kviz.model.Question;

import java.util.List;
import java.util.Optional;

public class QuestionRepository {
    public Question save(Question question) {
        EntityManager em = PersistenceManager.entityManager();

        try {
            em.getTransaction().begin();
            if (question.getId() == null) {
                em.persist(question);
            } else {
                question = em.merge(question);
            }
            em.getTransaction().commit();
            return question;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<Question> findById(Long id) {
        try (EntityManager em = PersistenceManager.entityManager()) {
            return Optional.ofNullable(em.find(Question.class, id));
        }
    }

    public List<Question> findAll() {
        try (EntityManager em = PersistenceManager.entityManager()) {
            TypedQuery<Question> q = em.createQuery("SELECT q FROM Question q", Question.class);
            return q.getResultList();
        }
    }

    public List<Question> findByQuiz(Quiz quiz){
        try (EntityManager em = PersistenceManager.entityManager()) {
            TypedQuery<Question> q = em.createQuery("SELECT q FROM Question q WHERE q.quiz.id = :id", Question.class);
            q.setParameter("id", quiz.getId());
            return q.getResultList();
        }
    }

    public void delete(Question question) {
        try (EntityManager em = PersistenceManager.entityManager()) {
            em.getTransaction().begin();
            em.remove(question);
            em.getTransaction().commit();
        }
    }
}
