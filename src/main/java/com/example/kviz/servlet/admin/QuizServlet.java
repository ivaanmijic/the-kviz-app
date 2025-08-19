package com.example.kviz.servlet.admin;

import com.example.kviz.model.Admin;
import com.example.kviz.model.Question;
import com.example.kviz.model.Quiz;
import com.example.kviz.model.dto.QuizDTO;
import com.example.kviz.service.AdminService;
import com.example.kviz.service.QuestionServices;
import com.example.kviz.service.QuizService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@WebServlet("/admin/quiz/*")
public class QuizServlet extends HttpServlet {

    private static final Logger log =  LoggerFactory.getLogger(QuizServlet.class);
    private static final String uploads = Paths.get(System.getProperty("user.dir"), "uploads").toString();

    private QuizService quizService;
    private QuestionServices questionServices;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        quizService = new QuizService();
        questionServices = new QuestionServices();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("QuizServlet: doGet");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String path = req.getPathInfo();
        if (path == null) {
            log.error("Resource not found");
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            gson.toJson(Map.of("error", "Resource not found"), resp.getWriter());
            return;
        }

        boolean myQuizzes = true;
        long adminId = 0;
        boolean publicQuizzes = false;

        if (path.equals("/list")) {
            try {
                adminId = Long.parseLong(req.getParameter("admin_id"));
            } catch (NumberFormatException e) {
                myQuizzes = false;
            }
        } else if (path.equals("/list/public")) {
            publicQuizzes = true;
        } else {
            log.error("Resource not found");
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            gson.toJson(Map.of("error", "Resource not found"), resp.getWriter());
            return;
        }

        try {
            List<Quiz> quizzes;
            String title;
            if (publicQuizzes) {
                HttpSession session = req.getSession();
                Admin user = (Admin) session.getAttribute("admin");
                if (user == null) {
                    throw new UnavailableException("Admin not logged in");
                }
                quizzes = quizService.findAllPublic(user.getId());
                title = "Public Quizzes";
            } else if (myQuizzes) {
                quizzes = quizService.findByOwnerId(adminId);
                title = "My Quizzes";
            } else {
                quizzes = quizService.findAll();
                title = "All Quizzes";
            }

            req.setAttribute("quizzes", quizzes);
            req.setAttribute("title", title);
            req.getRequestDispatcher("/WEB-INF/views/admin/quizzes.jsp").forward(req, resp);

        } catch (IllegalStateException e) {
            log.error("Bad request");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            gson.toJson(Map.of("error", e.getLocalizedMessage()), resp.getWriter());

        } catch (IllegalArgumentException e) {
            log.error("Error finding quizzes: {}", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            gson.toJson(Map.of("error", e.getLocalizedMessage()), resp.getWriter());

        } catch (RuntimeException e) {
            log.error("Error finding quizzes: {}", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            gson.toJson(Map.of("error", e.getLocalizedMessage()), resp.getWriter());
        } catch (UnavailableException e) {
            log.error("Error finding quizzes: {}", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
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
            Quiz quiz = quizService.findById(quizId).get();
            String quizImage = quiz.getThumbnail();

            File file = new File(uploads + "/quizImages/" + quizImage);
            file.delete();

            List<Question> questions = questionServices.findByQuizId((long) quizId);
            for (Question question : questions) {
                file = new File(uploads + "/questions/" + question.getImage());
                file.delete();
            }

            quizService.delete(quiz);

            log.info("Quiz with id {} has been deleted", quizId);
            resp.setStatus(HttpServletResponse.SC_OK);
            gson.toJson(Map.of("success", "Quiz has been deleted successfully."), resp.getWriter());

        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            gson.toJson(Map.of("error", e.getMessage()), resp.getWriter());

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
