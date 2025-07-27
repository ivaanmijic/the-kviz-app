package com.example.kviz.repository;

import com.example.kviz.database.PersistenceManager;
import com.example.kviz.model.Admin;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class AdminRepository {

    public Admin save(Admin admin) {
        EntityManager em = PersistenceManager.entityManager();

        try {
            em.getTransaction().begin();
            if (admin.getId() == null) {
                em.persist(admin);
            } else {
                admin = em.merge(admin);
            }
            em.getTransaction().commit();
            return admin;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<Admin> findByEmail(String email) {
        try (EntityManager em = PersistenceManager.entityManager()) {
            TypedQuery<Admin> query = em.createQuery("SELECT a FROM Admin a WHERE a.email = :email", Admin.class);
            query.setParameter("email", email);
            try {
                return Optional.ofNullable(query.getSingleResult());
            } catch (NoResultException e) {
                return Optional.empty();
            }
        }
    }

    public Optional<Admin> findByUsername(String username) {
        try (EntityManager em = PersistenceManager.entityManager()) {
            TypedQuery<Admin> query = em.createQuery("SELECT a FROM Admin a WHERE a.username = :username", Admin.class);
            query.setParameter("username", username);
            try {
                return Optional.ofNullable(query.getSingleResult());
            } catch (NoResultException e) {
                return Optional.empty();
            }
        }
    }

    public List<Admin> findAll() {
        try (EntityManager em = PersistenceManager.entityManager()) {
            TypedQuery<Admin> q = em.createQuery("SELECT a FROM Admin a", Admin.class);
            return q.getResultList();
        }
    }

    public void delete(Admin admin) {
        try (EntityManager em = PersistenceManager.entityManager()) {
            em.getTransaction().begin();
            Admin managedAdmin = em.contains(admin) ? admin : em.merge(admin);
            em.remove(managedAdmin);
            em.getTransaction().commit();
        }
    }

}
