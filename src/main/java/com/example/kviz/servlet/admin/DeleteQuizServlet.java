package com.example.kviz.servlet.admin;

import com.example.kviz.model.Question;
import com.example.kviz.model.Quiz;
import com.example.kviz.service.QuestionServices;
import com.example.kviz.service.QuizService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/deleteQuiz")
public class DeleteQuizServlet extends HttpServlet {
    QuizService quizService = new QuizService();
    QuestionServices questionServices = new QuestionServices();
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Quiz quiz = quizService.findById((long) id).get();
        quizService.delete(quiz);
        resp.sendRedirect("/admin");
    }
}
