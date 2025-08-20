package com.example.kviz.repository;

import com.example.kviz.database.PersistenceManager;
import com.example.kviz.model.Answer;
import com.example.kviz.model.Question;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AnswerRepository {
    public Answer save(Answer answer) {
        EntityManager em = PersistenceManager.entityManager();
        try {
            em.getTransaction().begin();
            if (answer.getId() == null) {
                em.persist(answer);
            } else {
                em.merge(answer);
            }
            em.getTransaction().commit();
            return answer;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    public List<Answer> findByQuestionId(long questionId){
        try (EntityManager em = PersistenceManager.entityManager()) {
            TypedQuery<Answer> a = em.createQuery("SELECT a FROM Answer a WHERE a.question.id = :id", Answer.class);
            a.setParameter("id", questionId);
            return a.getResultList();
        }
    }
    public void delete(Answer answer) {
        try (EntityManager em = PersistenceManager.entityManager()) {
            em.getTransaction().begin();
            em.remove(answer);
            em.getTransaction().commit();
        }
    }
}
