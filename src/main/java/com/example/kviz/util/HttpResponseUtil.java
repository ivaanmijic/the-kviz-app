package com.example.kviz.util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class HttpResponseUtil {
    public static void sendBadRequest(HttpServletResponse resp, String message) throws IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write(message);
    }

    public static void sendInternalServerError(HttpServletResponse resp, String message) throws IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        resp.getWriter().write(message);
    }
}
