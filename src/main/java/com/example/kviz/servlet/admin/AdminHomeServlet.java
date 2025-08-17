package com.example.kviz.servlet.admin;

import java.io.IOException;
import java.util.List;

import com.example.kviz.model.Quiz;
import com.example.kviz.service.QuizService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/home")
public class AdminHomeServlet extends HttpServlet {
  private final QuizService quizService = new QuizService();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    List<Quiz> myQuizzes = quizService.findAll();
    request.setAttribute("myQuizzes", myQuizzes);
    request.getRequestDispatcher("/WEB-INF/views/admin/home.jsp").forward(request, response);
  }
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Quiz> myQuizzes = quizService.findAll();
        request.setAttribute("myQuizzes", myQuizzes);
        request.getRequestDispatcher("/WEB-INF/views/admin/home.jsp").forward(request, response);
  }
}
