package com.example.kviz.servlet.admin;

import com.example.kviz.model.Admin;
import com.example.kviz.model.adapter.AdminTypeAdapter;
import com.example.kviz.model.request.AdminUpdateRequest;
import com.example.kviz.service.AdminService;
import com.example.kviz.service.SessionAuthTokenService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@WebServlet("/admin/update")
public class AdminUpdateServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AdminUpdateServlet.class);

    private AdminService adminService;
    private SessionAuthTokenService sessionAuthTokenService;
    private Gson gson;


    @Override
    public void init() throws ServletException {
        super.init();
        adminService = new AdminService();
        sessionAuthTokenService = new SessionAuthTokenService();

        gson = new GsonBuilder()
                .registerTypeAdapter(Admin.class, new AdminTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter out, LocalDateTime value) throws IOException {
                        out.value(value != null ? value.toString() : null);
                    }

                    @Override
                    public LocalDateTime read(JsonReader in) throws IOException {
                        return LocalDateTime.parse(in.nextString());
                    }
                })
                .create();
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
            gson.toJson(Map.of("error", "Error while processing request body"), resp.getWriter());
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

        if (payload == null || payload.getNewPassword() == null || payload.getAdmin().getPassword() == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            gson.toJson(Map.of("error", "Invalid requset payload. Old password is required"), resp.getWriter());
            return;
        }

        try {
            if (adminService.authenticateById(id, payload.getAdmin().getPassword())) {
                Admin updated = adminService.updateAdmin(id, payload.getAdmin(), payload.getNewPassword());
                resp.setStatus(HttpServletResponse.SC_OK);
                gson.toJson(Map.of("admin", updated), resp.getWriter());
                udpateSession(req.getSession(false), updated);
                updateCookie(req, updated);
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                gson.toJson(Map.of("error", "Wrong password"), resp.getWriter());
                log.warn("Failed authentication attempt for admin id: {}", id);
            }
        } catch (IllegalStateException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            gson.toJson(Map.of("error", e.getMessage()), resp.getWriter());
            log.error("Error while processing request", e);

        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error("Invalid arguments for admin update. {}", e.getMessage());
            gson.toJson(Map.of("error", e.getMessage()), resp.getWriter());

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            gson.toJson(Map.of("error", "A server error occurred while updating the admin profile."), resp.getWriter());
            log.error("Error updating admin", e);
        }

    }

    private void udpateSession(HttpSession session, Admin udpatedAdmin) {
        session.setAttribute("admin", udpatedAdmin);
        session.setAttribute("id", udpatedAdmin.getId());
        session.setAttribute("role", udpatedAdmin.getRole());
    }

    private void updateCookie(HttpServletRequest req, Admin updatedAdmin) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("admin")) {
                    if (cookie.getValue() != null) {
                        String token =  cookie.getValue();
                        sessionAuthTokenService.updateToken(token, updatedAdmin);
                    }
                }
            }
        }
    }
}
