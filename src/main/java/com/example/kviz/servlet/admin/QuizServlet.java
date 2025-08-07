package com.example.kviz.servlet.admin;

import com.example.kviz.model.Quiz;
import com.example.kviz.model.dto.QuizDTO;
import com.example.kviz.service.AdminService;
import com.example.kviz.service.QuizService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/admin/quiz/*")
public class QuizServlet extends HttpServlet {

    private static final Logger log =  LoggerFactory.getLogger(QuizServlet.class);

    private QuizService quizService;
    private AdminService adminService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        quizService = new QuizService();
        adminService = new AdminService();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Quiz servlet started");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        boolean myQuizzes = true;
        long adminId = 0;

        String path = req.getPathInfo();
        if (path == null || !path.equals("/list")) {
            log.error("Resource not found");
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            gson.toJson(Map.of("error", "Resource not found"), resp.getWriter());
            return;
        }

        try {
            adminId = Long.parseLong(req.getParameter("admin_id"));
        } catch (NumberFormatException e) {
            myQuizzes = false;
        }

        try {
            List<Quiz> quizzes = (myQuizzes)
                    ? quizService.findByOwnerId(adminId)
                    : quizService.findAll();
            List<QuizDTO> dtos = quizzes.stream()
                    .map(QuizDTO::fromEntity)
                    .collect(Collectors.toList());
            resp.setStatus(HttpServletResponse.SC_OK);
            gson.toJson(dtos, resp.getWriter());

        } catch (IllegalStateException e) {
            log.error("Bad request");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            gson.toJson(Map.of("error", e.getLocalizedMessage()), resp.getWriter());

        } catch (IllegalArgumentException e) {
            log.error("Error finding quizzes: {}",  e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            gson.toJson(Map.of("error", e.getLocalizedMessage()), resp.getWriter());

        } catch (RuntimeException e) {
            log.error("Error finding quizzes: {}",  e.getMessage());
            resp.setStatus(500);
            gson.toJson(Map.of("error", e.getLocalizedMessage()), resp.getWriter());
        }

    }
}
