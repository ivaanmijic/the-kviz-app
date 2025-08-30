package com.example.kviz.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/admin/panel")
public class AdminPanelServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AdminPanelServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Admin panel requested");
        req.getRequestDispatcher("/WEB-INF/views/admin/panel.jsp").forward(req, resp);
    }
}
