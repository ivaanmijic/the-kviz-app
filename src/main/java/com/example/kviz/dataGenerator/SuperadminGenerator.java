package com.example.kviz.dataGenerator;

import com.example.kviz.model.Admin;
import com.example.kviz.model.supporting.AdminRole;
import com.example.kviz.service.AdminService;

public class SuperadminGenerator {
    private static final AdminService adminService = new AdminService();

    public static void main(String[] args) {
        Admin superadmin = new Admin("superadmin@fet.ba", "superadmin", "admin123", AdminRole.SUPERADMIN);
        adminService.registerAdmin(superadmin);
    }
}
