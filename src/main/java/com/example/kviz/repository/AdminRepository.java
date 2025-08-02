package com.example.kviz.repository;

import com.example.kviz.database.PersistenceManager;
import com.example.kviz.model.Admin;

import com.example.kviz.model.supporting.AdminRole;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class AdminRepository {

    private static final Logger log = LoggerFactory.getLogger(AdminRepository.class);

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

    public Optional<Admin> findById(Long id) {
        try (EntityManager em = PersistenceManager.entityManager()) {
            return Optional.ofNullable(em.find(Admin.class, id));
        } catch (NoResultException e) {
            return Optional.empty();
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

    public List<Admin> findByRole(AdminRole role) {
        try (EntityManager em = PersistenceManager.entityManager()) {
            TypedQuery<Admin> query = em.createQuery("SELECT a FROM Admin a WHERE a.role = :role", Admin.class);
            query.setParameter("role", role);
            return query.getResultList();
        }
    }

    public List<Admin> findAll() {
        try (EntityManager em = PersistenceManager.entityManager()) {
            TypedQuery<Admin> q = em.createQuery("SELECT a FROM Admin a", Admin.class);
            return q.getResultList();
        }
    }

    public void delete(Admin admin) {
        EntityManager em = PersistenceManager.entityManager();

        try {
            em.getTransaction().begin();

            em.createQuery("Delete from SessionAuthToken t WHERE t.admin.id = :id")
                    .setParameter("id", admin.getId())
                    .executeUpdate();

            em.createQuery("Delete from Quiz q WHERE q.owner.id = :id")
                    .setParameter("id", admin.getId())
                    .executeUpdate();

            em.createQuery("Delete from Admin a WHERE a.id = :id")
                    .setParameter("id", admin.getId())
                    .executeUpdate();

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

    public void deleteById(Long id) {
                EntityManager em = PersistenceManager.entityManager();

        try {
            em.getTransaction().begin();

            em.createQuery("Delete from SessionAuthToken t WHERE t.admin.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            em.createQuery("Delete from Quiz q WHERE q.owner.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            em.createQuery("Delete from Admin a WHERE a.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

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
