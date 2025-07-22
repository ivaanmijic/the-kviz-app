package com.example.kviz.service;

import com.example.kviz.repository.AdminRepository;

import java.util.Optional;

public class AdminService {

    private AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

}
