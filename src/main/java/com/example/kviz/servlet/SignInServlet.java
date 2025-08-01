package com.example.kviz.servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.kviz.model.Admin;
import com.example.kviz.service.AdminService;
import com.example.kviz.service.SessionAuthTokenService;
import com.example.kviz.util.HttpResponseUtil;
import com.example.kviz.util.IdentifierChecker;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/signin")
public class SignInServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(SignInServlet.class);

    private AdminService adminService;
    private SessionAuthTokenService sessionAuthTokenService;
    private Gson gson;

    private static class SignInRequest {
        private String emailOrUsername;
        private String password;
        private boolean rememberMe;

        public SignInRequest() {}

        public String getEmailOrUsername() {
            return emailOrUsername;
        }

        public void setEmailOrUsername(String emailOrUsername) {
            this.emailOrUsername = emailOrUsername;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isRememberMe() {
            return rememberMe;
        }

        public void setRememberMe(boolean rememberMe) {
            this.rememberMe = rememberMe;
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        this.adminService = new AdminService();
        this.sessionAuthTokenService = new SessionAuthTokenService();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("message", "Hello from test servlet");
        req.getRequestDispatcher("/WEB-INF/views/sign-in.jsp").forward(req, resp);
        log.info("Sign-in GET request received");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        log.info("Sign-in POST request received");
        String body = req.getReader().lines().collect(Collectors.joining("\n"));

        SignInRequest signInRequest = gson.fromJson(body, SignInRequest.class);
        String emailOrUsername = signInRequest.getEmailOrUsername();
        String password = signInRequest.getPassword();
        boolean rememberMe = signInRequest.isRememberMe();

        if (emailOrUsername == null || emailOrUsername.trim().isEmpty()) {
            HttpResponseUtil.sendBadRequest(resp, "Email or username is required");
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            HttpResponseUtil.sendBadRequest(resp, "Password is required");
            return;
        }

        if (adminService.authenticate(emailOrUsername, password)) {
            IdentifierChecker.IdentifierType  identifierType = IdentifierChecker.identify(emailOrUsername);

            Optional<Admin> optionalAdmin;
            if (identifierType == IdentifierChecker.IdentifierType.EMAIL) {
                optionalAdmin = adminService.getAdminByEmail(emailOrUsername);
            } else if  (identifierType == IdentifierChecker.IdentifierType.USERNAME) {
                optionalAdmin = adminService.getAdminByUsername(emailOrUsername);
            } else { return; }

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
                    tokenCookie.setPath(req.getContextPath());

                    resp.addCookie(tokenCookie);
                }

                resp.sendRedirect(req.getContextPath() + "admin/home");
            } else {
                HttpResponseUtil.sendBadRequest(resp, "Invalid email/username or password");
            }
        } else {
            HttpResponseUtil.sendBadRequest(resp, "Invalid email/username or password");
        }
    }


}
