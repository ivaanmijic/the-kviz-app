package com.example.kviz.repository;

import com.example.kviz.database.PersistenceManager;
import com.example.kviz.model.Admin;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class AdminRepository {

    public Admin save(Admin admin) {
        EntityManager em = PersistenceManager.createEntityManager();

        try {
            em.getTransaction();
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

    public Optional<Admin> findById(Long id) {
        try (EntityManager em = PersistenceManager.createEntityManager()) {
            return Optional.ofNullable(em.find(Admin.class, id));
        }
    }

    public Optional<Admin> findByEmail(String email) {
        try (EntityManager em = PersistenceManager.createEntityManager()) {
            return Optional.ofNullable(em.find(Admin.class, email));
        }
    }

    public Optional<Admin> findByUsername(String username) {
        try (EntityManager em = PersistenceManager.createEntityManager()) {
            return Optional.ofNullable(em.find(Admin.class, username));
        }
    }

    public List<Admin> findAll() {
        try (EntityManager em = PersistenceManager.createEntityManager()) {
            TypedQuery<Admin> q = em.createQuery("SELECT a FROM Admin a", Admin.class);
            return q.getResultList();
        }
    }

    public void delete(Admin admin) {
        try (EntityManager em = PersistenceManager.createEntityManager()) {
            em.getTransaction().begin();
            em.remove(admin);
            em.getTransaction().commit();
        }
    }

}
