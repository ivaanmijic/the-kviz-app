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
        log.info("QuizServlet: doGet");

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

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("QuizServlet: doDelete request received");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String path = req.getPathInfo();

        if (path == null || path.equals("/") || path.split("/").length != 2) {
            log.error("Invalid path for DELETE request: {}", path);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            gson.toJson(Map.of("error", "Invalid request. URL must be in the format /quiz/{id}"), resp.getWriter());
            return;
        }

        long quizId;
        try {
            quizId = Long.parseLong(path.substring(1));
        } catch (NumberFormatException e) {
            log.error("Invalid quiz ID format in path: {}", path);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            gson.toJson(Map.of("error", "Invalid quiz ID format."), resp.getWriter());
            return;
        }

        try {
            quizService.deleteById(quizId);
            log.info("Quiz with id {} has been deleted", quizId);
            resp.setStatus(HttpServletResponse.SC_OK);
            gson.toJson(Map.of("success", "Quiz has been deleted successfully."), resp.getWriter());

        } catch (IllegalArgumentException e) {
            log.error("Bad request during quiz deletion: {}", e.getMessage(), e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            gson.toJson(Map.of("error", e.getLocalizedMessage()), resp.getWriter());

        } catch (Exception e) {
            log.info(e.getMessage());
            log.error("An unexpected error occurred during quiz deletion: {}", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            gson.toJson(Map.of("error", "An unexpected error occurred while trying to delete the quiz."), resp.getWriter());
        }
    }
}
