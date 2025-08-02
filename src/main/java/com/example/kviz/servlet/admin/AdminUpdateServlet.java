package com.example.kviz.servlet.admin;

import com.example.kviz.model.Admin;
import com.example.kviz.model.supporting.AdminRole;
import com.example.kviz.service.AdminService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import jakarta.jms.Session;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@WebServlet("/admin/update")
public class AdminUpdateServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AdminUpdateServlet.class);

    private AdminService adminService;
    private Gson gson;

    private static class AdminUpdateRequest {
        @Expose
        String username;
        @Expose
        String email;
        @Expose
        AdminRole role;
        @Expose
        String currentPassword;
        @Expose
        String newPassword;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        adminService = new AdminService();
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        long id;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            gson.toJson(Map.of("error", "Invalid id"), resp.getWriter());
            return;
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            log.error("Error while processing request", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            gson.toJson(Map.of("error", "Error while processing request"), resp.getWriter());
            return;
        }

        String jsonBody = sb.toString();
        log.info("json body: {}", jsonBody);
        AdminUpdateRequest payload;
        try {
            payload = gson.fromJson(jsonBody, AdminUpdateRequest.class);
        } catch (JsonSyntaxException e) {
            gson.toJson(Map.of("error", "Invalid JSON"), resp.getWriter());
            log.error("Invalid JSON.");
            return;
        }

        try {
            Admin admin = new Admin(payload.email, payload.username, payload.currentPassword, payload.role);
            if (adminService.authenticateById(id, payload.currentPassword)) {
                Admin updated = adminService.updateAdmin(id, admin, payload.newPassword);
                resp.setStatus(HttpServletResponse.SC_OK);
                gson.toJson(Map.of("admin", updated), resp.getWriter());
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                gson.toJson(Map.of("error", "Wrong password"), resp.getWriter());
                log.error("Wrong password.");
            }
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error("Invalid arguments. {}", e.getMessage());
            gson.toJson(Map.of("error", e.getMessage()), resp.getWriter());

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            gson.toJson(Map.of("error", "Server error"), resp.getWriter());
            log.error("Error updating admin", e);
        }

    }

    private void udpateSession(HttpServletRequest req, Admin udpatedAdmin) {
    }
}
