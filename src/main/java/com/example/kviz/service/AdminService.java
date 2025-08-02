package com.example.kviz.service;

import com.example.kviz.model.Admin;
import com.example.kviz.model.supporting.AdminRole;
import com.example.kviz.repository.AdminRepository;
import com.example.kviz.util.PasswordUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService() {
        this.adminRepository = new AdminRepository();
    }

    public Optional<Admin> getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public Optional<Admin> getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public List<Admin> getAllEditors() {
        return adminRepository.findByRole(AdminRole.EDITOR);
    }

    public Admin updateAdmin(Long id, Admin admin, String newPassword) {
        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() ->  new EntityNotFoundException("Admin with ID: " + id + " not found"));

        if (adminRepository.findByEmail(admin.getEmail()).isPresent() &&
                !adminRepository.findByEmail(admin.getEmail()).get().getEmail().equals(admin.getEmail())) {
            throw new IllegalStateException("An account with this email already exists. Choose another one.");
        }

        if (adminRepository.findByUsername(admin.getUsername()).isPresent() &&
                !adminRepository.findByUsername(admin.getUsername()).get().getUsername().equals(admin.getUsername())) {
            throw new IllegalStateException("An account with username already exists. Choose another one.");
        }

        existingAdmin.setEmail(admin.getEmail());
        existingAdmin.setUsername(admin.getUsername());
        existingAdmin.setRole(admin.getRole());

        if (newPassword != null && !newPassword.trim().isEmpty()) {
            existingAdmin.setPassword(PasswordUtil.hashPassword(newPassword));
        }

        return adminRepository.save(existingAdmin);
    }

    public Admin registerAdmin(Admin admin) throws IllegalStateException, PersistenceException {
        if (adminRepository.findByUsername(admin.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already exists");
        }
        if (adminRepository.findByEmail(admin.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        admin.setPassword(PasswordUtil.hashPassword(admin.getPassword()));

        try {
            return adminRepository.save(admin);
        } catch (PersistenceException e) {
            throw new PersistenceException(e.getMessage());
        }

    }

    public void deleteAdminById(Long id) {
        adminRepository.deleteById(id);
    }

    public boolean authenticate(String identificator, String password) {
        return authenticateByEmail(identificator, password) || authenticateByUsername(identificator, password);
    }

    public boolean authenticateById(Long id, String password) {
        Optional<Admin> adminOptional = adminRepository.findById(id);
        if (adminOptional.isPresent()) {
            LoggerFactory.getLogger(AdminService.class).info("Admin with ID: " + id + " authenticated");
            LoggerFactory.getLogger(AdminService.class).info("Password: " + password);
        }
        return adminOptional.map(admin ->
                PasswordUtil.checkPassword(password, admin.getPassword())
        ).orElse(false);
    }

    private boolean authenticateByEmail(String email, String password) {
        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);

        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            return PasswordUtil.checkPassword(password, admin.getPassword());
        }
        return false;
    }

    private boolean authenticateByUsername(String username, String password) {
        Optional<Admin> optionalAdmin = adminRepository.findByUsername(username);

        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            return PasswordUtil.checkPassword(password, admin.getPassword());
        }
        return false;
    }

}
