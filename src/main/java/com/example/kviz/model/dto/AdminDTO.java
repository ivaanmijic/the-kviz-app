package com.example.kviz.model.dto;

import com.example.kviz.model.Admin;

public class AdminDTO {
    private String id;
    private String username;
    private String email;
    private String role;

    public static AdminDTO fromEntity(Admin admin) {
        AdminDTO dto = new AdminDTO();
        dto.id = admin.getId().toString();
        dto.username = admin.getUsername();
        dto.email = admin.getEmail();
        dto.role = admin.getRole().toString();
        return dto;
    }
}
