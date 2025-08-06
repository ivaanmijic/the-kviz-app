package com.example.kviz.filter;

import com.example.kviz.model.Admin;
import com.example.kviz.service.SessionAuthTokenService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class RememberMeCookieFilter implements Filter {
    private SessionAuthTokenService sessionAuthTokenService;
    private static final Logger logger = LoggerFactory.getLogger(RememberMeCookieFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        this.sessionAuthTokenService = new SessionAuthTokenService();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        boolean isLoggedIn = session != null && session.getAttribute("admin") != null;

        if (!isLoggedIn) {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("rememberMe".equals(cookie.getName())) {
                        String token = cookie.getValue();
                        Optional<Admin> optionalAdmin = sessionAuthTokenService.getAdminByToken(token);

                        if (optionalAdmin.isPresent()) {
                            Admin admin = optionalAdmin.get();
                            session = req.getSession(true);
                            session.setAttribute("admin", admin);
                            session.setAttribute("username", admin.getUsername());
                            session.setAttribute("role", admin.getRole());
                            session.setMaxInactiveInterval(3600);
                            logger.info("Session initialized for: {}", admin.getUsername());
                        } else {
                            cookie.setMaxAge(0);
                            cookie.setPath("/");
                            resp.addCookie(cookie);
                            logger.warn("Invalid rememberMe token; cookie cleared.");
                        }
                    }
                }
            }
        }

        if ((req.getRequestURI().equals("/") ||
                req.getRequestURI().equals("/auth/")) &&
                req.getSession(false) != null &&
                req.getSession(false).getAttribute("admin") != null) {
            resp.sendRedirect("/admin/view");
            return;
        }

        chain.doFilter(req, resp);
    }

}
