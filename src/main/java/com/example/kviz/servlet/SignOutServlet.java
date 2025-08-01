package com.example.kviz.servlet;


import com.example.kviz.service.SessionAuthTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/signout")
public class SignOutServlet extends HttpServlet {

    private SessionAuthTokenService sessionAuthTokenService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.sessionAuthTokenService = new SessionAuthTokenService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("rememberMe")) {
                    String token = cookie.getValue();
                    sessionAuthTokenService.deleteToken(token);

                    cookie.setMaxAge(0);
                    cookie.setPath(req.getContextPath());
                    resp.addCookie(cookie);
                }
            }
        }

        req.getSession().invalidate();
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
