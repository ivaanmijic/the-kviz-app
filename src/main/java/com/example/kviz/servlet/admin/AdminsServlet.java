package com.example.kviz.servlet.admin;

import com.example.kviz.model.Admin;
import com.example.kviz.model.adapter.AdminTypeAdapter;
import com.example.kviz.model.dto.AdminDTO;
import com.example.kviz.model.request.AdminUpdateRequest;
import com.example.kviz.service.AdminService;
import com.example.kviz.service.SessionAuthTokenService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/admins/*")
public class AdminsServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AdminsServlet.class);

    private AdminService adminService;
    private SessionAuthTokenService  sessionAuthTokenService;

    private Gson gson;
    private Gson bodyGson;


    @Override
    public void init() throws ServletException {
        super.init();
        adminService = new AdminService();
        sessionAuthTokenService = new SessionAuthTokenService();
        gson = new Gson();
        bodyGson = new GsonBuilder()
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("AdminsServlet: doGet");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            handleAdminListRequest(req, resp);
            return;
        }

        String[] parts = pathInfo.split("/");
        if (parts.length == 3 && "profile".equals(parts[2])) {
            handleAdminProfileRequest(req, resp);
        } else {
            log.error("AdminsServlet: doGet: invalid request: {}", pathInfo);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            gson.toJson(Map.of("error", "invalid request"), resp.getWriter());
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("AdminsServlet doDelete");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String path;
        long adminId;
        try {
            path = req.getPathInfo();
            adminId = Long.parseLong(path.substring(1));

        } catch (IllegalArgumentException e) {
            log.error("AdminsServlet doDelete error", e);
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            gson.toJson(Map.of("error", "Resource not found"), resp.getWriter());

            return;
        }

        try {
            adminService.deleteAdminById(adminId);

            if (Boolean.parseBoolean(req.getParameter("self"))) {
                log.info("AdminsServlet doDelete self, redirecting to /auth/logout");
                resp.sendRedirect("/auth/logout");
                return;
            }

            log.info("Successfully delete admin with id {}", adminId);
            gson.toJson(Map.of("success", "Admin has been deleted"), resp.getWriter());

        } catch (IllegalArgumentException e) {
            log.error("AdminsServlet doDelete error", e);
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            gson.toJson(Map.of("error", "Resource not found"), resp.getWriter());

        }
        catch (Exception e) {
            log.error("AdminsServlet doDelete error", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            gson.toJson(Map.of("error", e.getMessage()), resp.getWriter());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("AdminsServlet doPut");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String path;
        long adminId;
        try {
            path = req.getPathInfo();
            adminId = Long.parseLong(path.substring(1));

        } catch (IllegalArgumentException e) {
            log.error("AdminsServlet doDelete error", e);
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            gson.toJson(Map.of("error", "Resource not found"), resp.getWriter());

            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = req.getReader()) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            log.error("AdminsServlet doPut error", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            gson.toJson(Map.of("error", "Error while processing request"), resp.getWriter());
            return;
        }

        String body = stringBuilder.toString();
        AdminUpdateRequest payload;
        try {
            payload = bodyGson.fromJson(body, AdminUpdateRequest.class);
        } catch (JsonSyntaxException e) {
            log.error("AdminsServlet doPut error", e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            gson.toJson(Map.of("error", e.getMessage()), resp.getWriter());
            return;
        }

        if (payload == null || payload.getNewPassword() == null || payload.getAdmin().getPassword() == null) {
            log.error("AdminsServlet doPut error");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            gson.toJson(Map.of("error", "Missing required parameters"), resp.getWriter());
            return;
        }

        try {
            if (adminService.authenticateById(adminId, payload.getAdmin().getPassword())) {
                Admin updated = adminService.updateAdmin(adminId, payload.getAdmin(), payload.getNewPassword());
                resp.setStatus(HttpServletResponse.SC_OK);
                gson.toJson(Map.of("success", "Account has been updated"), resp.getWriter());
                updateSession(req.getSession(false), updated);
                updateCookie(req, updated);
            } else {
                log.warn("AdminsServlet doPut unauthorized request");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                gson.toJson(Map.of("error", "Unauthorized"), resp.getWriter());
            }
        } catch (IllegalStateException e) {
            log.error("AdminsServlet doPut error", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            gson.toJson(Map.of("error", "Error while processing request"), resp.getWriter());
        } catch (IllegalArgumentException e) {
            log.error("AdminsServlet doPut error", e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            gson.toJson(Map.of("error", "Missing required parameters"), resp.getWriter());
        } catch (JsonSyntaxException e) {
            log.error("AdminsServlet doPut error", e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            gson.toJson(Map.of("error", "Error while processing request"), resp.getWriter());
        }
    }

    private void updateSession(HttpSession session, Admin udpatedAdmin) {
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

    private void handleAdminProfileRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idPart = req.getPathInfo().split("/")[1];
        try {
            long adminId = Long.parseLong(idPart);
            Admin admin = adminService.getAdminById(adminId);
            req.setAttribute("admin", admin);
            req.getRequestDispatcher("/WEB-INF/views/admin/profile.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            log.error("AdminsProfileServlet doGet: invalid admin id {}", idPart);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            gson.toJson(Map.of("error", "Invalid admin id " + idPart), resp.getWriter());

        } catch (EntityNotFoundException e) {
            log.error("AdminsProfileServlet doGet: Admin with id {} not found", idPart);
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            gson.toJson(Map.of("error", "Admin with id " + idPart + " not found"), resp.getWriter());
        }
    }

    private void handleAdminListRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Admin> admins = adminService.getAllAdmins();
            List<AdminDTO> dtos = admins.stream()
                    .map(AdminDTO::fromEntity)
                    .toList();
            gson.toJson(Map.of("editors", dtos), resp.getWriter());

        } catch (Exception e) {
            log.error("AdminsServlet: doGet: Error", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            bodyGson.toJson(Map.of("error", "Error"),  resp.getWriter());
        }
    }
}
