package com.example.kviz.servlet;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.kviz.model.Admin;
import com.example.kviz.service.AdminService;
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
    private Gson gson;

    private static class SignInRequest {
        private String emailOrUsername;
        private String password;

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
    }

    @Override
    public void init() throws ServletException {
        super.init();
        this.adminService = new AdminService();
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
        String rememberMe = req.getParameter("rememberMe");

        SignInRequest signInRequest = gson.fromJson(body, SignInRequest.class);
        String emailOrUsername = signInRequest.getEmailOrUsername();
        String password = signInRequest.getPassword();

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

                if (rememberMe != null && rememberMe.equals("true")) {
                    Cookie usernameCookie = new Cookie("username", admin.getUsername());
                    usernameCookie.setMaxAge(7 *  24 * 60 * 60);
                    usernameCookie.setHttpOnly(true);
                    usernameCookie.setSecure(req.isSecure());
                    usernameCookie.setPath(req.getContextPath());
                    resp.addCookie(usernameCookie);
                }

                resp.sendRedirect(req.getContextPath() + "/admin/test");
            } else {
                HttpResponseUtil.sendBadRequest(resp, "Invalid email/username or password");
            }
        } else {
            HttpResponseUtil.sendBadRequest(resp, "Invalid email/username or password");
        }
    }


}
