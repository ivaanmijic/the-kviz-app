package com.example.kviz.servlet.admin;

import com.example.kviz.service.AdminService;
import com.example.kviz.util.HttpResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/admin/delete")
public class AdminDeleteServlet extends HttpServlet {
    private AdminService adminService;
    private static final Logger log = LoggerFactory.getLogger(AdminDeleteServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();
        adminService = new AdminService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("AdminDeleteServlet: doPost:");

        Long id;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            log.error("Invalid id: {}", String.valueOf(e));
            HttpResponseUtil.sendBadRequest(resp, e.getMessage());
            return;
        }

        try {
            adminService.deleteAdminById(id);

            if (Boolean.parseBoolean(req.getParameter("self"))) {
                log.info("Deleting self, forwading to /signout...");
                resp.sendRedirect("/signout");
                return;
            }

            resp.getWriter().println("Deleted admin with id: " + id);
            log.info("AdminDeleteServlet: doPost: deleted admin with id: {}", id);

        } catch (IllegalArgumentException e) {
            log.error("Invalid id: {}", String.valueOf(e));
            HttpResponseUtil.sendBadRequest(resp, e.getMessage());

        } catch (RuntimeException e) {
            log.error("AdminDeleteServlet: doPost: error", e);
            HttpResponseUtil.sendInternalServerError(resp, e.getMessage());
        }
    }
}
