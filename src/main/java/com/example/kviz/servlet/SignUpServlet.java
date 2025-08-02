package com.example.kviz.servlet;

import com.example.kviz.model.Admin;
import com.example.kviz.model.request.SignUpRequest;
import com.example.kviz.model.supporting.AdminRole;
import com.example.kviz.service.AdminService;
import com.example.kviz.util.HttpResponseUtil;
import com.example.kviz.util.IdentifierChecker;
import com.google.gson.Gson;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(SignUpServlet.class);

    private AdminService adminService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        this.adminService = new AdminService();
        this.gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("SignUpServlet doPost request received");

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

        Admin admin = new Admin(
                signUp.getEmail(),
                signUp.getUsername(),
                signUp.getPassword(),
                AdminRole.EDITOR
        );

        try {
            Admin registerAdmin = adminService.registerAdmin(admin);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(gson.toJson(registerAdmin.getUsername()));
        } catch (IllegalStateException e) {
            HttpResponseUtil.sendBadRequest(resp, e.getMessage());
        } catch (PersistenceException e) {
            log.error(e.getMessage());
            HttpResponseUtil.sendInternalServerError(resp, "Unexpected error");
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
