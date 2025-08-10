package com.example.kviz.servlet.admin;

import com.example.kviz.model.Admin;
import com.example.kviz.service.AdminService;
import com.google.gson.Gson;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class AdminsProfileServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AdminsProfileServlet.class);

    private AdminService adminService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        adminService = new AdminService();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("AdminsProfileServlet doGet");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");



    }
}
