package com.example.kviz.servlet;

import com.example.kviz.model.Admin;
import com.example.kviz.model.request.SignInRequest;
import com.example.kviz.model.request.SignUpRequest;
import com.example.kviz.model.supporting.AdminRole;
import com.example.kviz.service.AdminService;
import com.example.kviz.service.SessionAuthTokenService;
import com.example.kviz.util.HttpResponseUtil;
import com.example.kviz.util.IdentifierChecker;
import com.google.gson.Gson;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AuthServlet.class);

    private AdminService adminService;
    private SessionAuthTokenService sessionAuthTokenService;

    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        this.adminService = new AdminService();
        this.sessionAuthTokenService = new SessionAuthTokenService();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getPathInfo();

        if (action == null || action.isBlank()) {
            log.info("No action provided");
            req.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(req, resp);
        } else if  (action.equals("/logout")) {
            handleLogout(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getPathInfo();

        if (action == null) {
            HttpResponseUtil.sendBadRequest(resp, "No action specified");
            return;
        }

        switch (action) {
            case "/register":
                handleRegister(req, resp);
                break;
            case "/login":
                handleLogin(req, resp);
                break;
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("handle register request");

        String body = req.getReader().lines().collect(Collectors.joining("\n"));
        SignUpRequest signUp = gson.fromJson(body, SignUpRequest.class);

        List<String> errors = new ArrayList<>();
        if (isEmpty(signUp.getEmail())) errors.add("Email is required");
        else if (!IdentifierChecker.isEmail(signUp.getEmail())) errors.add("Invalid email");

        if (isEmpty(signUp.getUsername())) errors.add("Username is required");
        else if (!IdentifierChecker.isUsername(signUp.getUsername())) errors.add("Invalid username");

        if (isEmpty(signUp.getPassword())) errors.add("Password is required");
        else if (signUp.getPassword().length() < 8) errors.add("Password length must be at least 8 characters");

        if (!errors.isEmpty()) {
            HttpResponseUtil.sendBadRequest(resp, String.join("; ", errors));
            return;
        }

        Admin admin = new Admin(signUp.getEmail(), signUp.getUsername(), signUp.getPassword(), AdminRole.EDITOR);

        try {
            Admin registered = adminService.registerAdmin(admin);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(gson.toJson(registered.getUsername()));
        } catch (IllegalStateException e) {
            HttpResponseUtil.sendBadRequest(resp, e.getMessage());
        } catch (PersistenceException e) {
            log.error(e.getMessage());
            HttpResponseUtil.sendInternalServerError(resp, "Unexpected error");
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("handle login request");

        String body = req.getReader().lines().collect(Collectors.joining("\n"));

        SignInRequest signIn = gson.fromJson(body, SignInRequest.class);
        String emailOrUsername = signIn.getEmailOrUsername();
        String password = signIn.getPassword();
        boolean rememberMe = signIn.isRememberMe();

        if (emailOrUsername == null || emailOrUsername.trim().isEmpty()) {
            HttpResponseUtil.sendBadRequest(resp, "Email or username is required");
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            HttpResponseUtil.sendBadRequest(resp, "Password is required");
            return;
        }

        if (adminService.authenticate(emailOrUsername, password)) {
            IdentifierChecker.IdentifierType identifierType = IdentifierChecker.identify(emailOrUsername);

            Optional<Admin> optionalAdmin;
            if (identifierType == IdentifierChecker.IdentifierType.EMAIL) {
                optionalAdmin = adminService.getAdminByEmail(emailOrUsername);
            } else if (identifierType == IdentifierChecker.IdentifierType.USERNAME) {
                optionalAdmin = adminService.getAdminByUsername(emailOrUsername);
            } else {
                return;
            }

            if (optionalAdmin.isPresent()) {
                Admin admin = optionalAdmin.get();

                HttpSession session = req.getSession(true);
                session.setAttribute("admin", admin);
                session.setAttribute("username", admin.getUsername());
                session.setAttribute("role", admin.getRole());

                session.setMaxInactiveInterval(3600);

                if (rememberMe) {
                    String token = UUID.randomUUID().toString();
                    LocalDateTime expiration = LocalDateTime.now().plusDays(7);

                    sessionAuthTokenService.createToken(token, admin, expiration);

                    Cookie tokenCookie = new Cookie("rememberMe", token);
                    tokenCookie.setMaxAge(7 * 24 * 60 * 60);
                    tokenCookie.setHttpOnly(true);
                    tokenCookie.setSecure(req.isSecure());
                    tokenCookie.setPath("/");

                    resp.addCookie(tokenCookie);
                }

                resp.sendRedirect("/admin/panel");
            } else {
                HttpResponseUtil.sendBadRequest(resp, "Invalid email/username or password");
            }
        } else {
            HttpResponseUtil.sendBadRequest(resp, "Invalid email/username or password");
        }
    }

    private void handleLogout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("handle logout request");

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("rememberMe")) {
                    String token = cookie.getValue();
                    sessionAuthTokenService.deleteToken(token);

                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    resp.addCookie(cookie);
                }
            }
        }

        req.getSession().invalidate();
        resp.sendRedirect(req.getContextPath() + "/auth");
    }


    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
