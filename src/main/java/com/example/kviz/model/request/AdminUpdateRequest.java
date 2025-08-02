package com.example.kviz.model.request;

import com.example.kviz.model.Admin;
import com.google.gson.annotations.Expose;

public class AdminUpdateRequest {
    @Expose
    private Admin admin;

    @Expose
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public Admin getAdmin() {
        return admin;
    }
}
