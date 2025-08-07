package com.example.kviz.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/admin/view/*")
public class AdminViewController extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AdminViewController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Admin view controller");

        String path = req.getPathInfo();

        if (path == null || path.equals("/") || path.isBlank()) {
            log.info("Main view requested");
            req.getRequestDispatcher("/WEB-INF/views/admin/home.jsp").forward(req, resp);
            return;
        }

        String view = path.substring(1);
        switch (view) {
            case "dashboard":
                log.info("Dashboard view");
                req.getRequestDispatcher("/WEB-INF/views/admin/dashboard-view.jsp").forward(req, resp);
                break;
            case "quizzes":
                log.info("Quizzes requested");
                req.getRequestDispatcher("/WEB-INF/views/admin/quizzes-view.jsp").forward(req, resp);
                break;
            case "create-quiz":
                log.info("New quiz requested");
                req.getRequestDispatcher("/WEB-INF/views/admin/create-quiz-view.jsp").forward(req, resp);
                break;
            case "profile":
                log.info("Profile requested");
                req.getRequestDispatcher("/WEB-INF/views/admin/profile-view.jsp").forward(req, resp);
                break;
            case "admin-list":
                log.info("All admins requested");
                req.getRequestDispatcher("/WEB-INF/views/admin/admin-list-view.jsp").forward(req, resp);
                break;
            case "all-quizzes":
                log.info("All Quizzes requested");
                req.getRequestDispatcher("/WEB-INF/views/admin/all-quizzes-view.jsp").forward(req, resp);
                break;
            case "logs":
                log.info("Logs requested");
                req.getRequestDispatcher("/WEB-INF/views/admin/logs-view.jsp").forward(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
