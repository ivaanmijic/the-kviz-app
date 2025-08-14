package com.example.kviz.model.dto;

import com.example.kviz.model.Admin;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AdminDTO {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm", Locale.ENGLISH);

    private String id;
    private String username;
    private String email;
    private String role;
    private String dateCreated;
    private String dateUpdated;

    public static AdminDTO fromEntity(Admin admin) {
        AdminDTO dto = new AdminDTO();
        dto.id = admin.getId().toString();
        dto.username = admin.getUsername();
        dto.email = admin.getEmail();
        dto.role = admin.getRole().toString();
        dto.dateCreated = admin.getCreatedAt().format(FORMATTER);
        dto.dateUpdated = admin.getUpdatedAt() != null ?
                admin.getUpdatedAt().format(FORMATTER)
                : null;
        return dto;
    }
}
