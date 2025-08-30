package com.example.kviz.servlet.admin;

import com.example.kviz.model.Admin;
import com.example.kviz.model.Quiz;
import com.example.kviz.model.adapter.AdminTypeAdapter;
import com.example.kviz.model.supporting.QuizDTO;
import com.example.kviz.service.AdminService;
import com.example.kviz.service.QuizService;
import com.example.kviz.service.SessionAuthTokenService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@WebServlet("/admin/quiz-information")
public class QuizInformationServlet extends HttpServlet {
    private QuizService quizService;
    private Gson gson;

    public void init() throws ServletException {
        super.init();
        quizService = new QuizService();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter out, LocalDateTime value) throws IOException {
                        out.value(value != null ? value.toString() : null);
                    }

                    @Override
                    public LocalDateTime read(JsonReader in) throws IOException {
                        return LocalDateTime.parse(in.nextString());
                    }
                })
                .create();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         int quizId = Integer.parseInt(request.getParameter("id"));
         Optional<Quiz> opt = quizService.findById((long) quizId);
         Quiz quiz = opt.get();
         System.out.println(quiz.getId());
         System.out.println(quiz.getTitle());
         System.out.println(quiz.getDescription());
         QuizDTO dto = QuizDTO.fromEntity(quiz);


         response.setContentType("application/json");
         response.setCharacterEncoding("UTF-8");

         Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
         String json = gson.toJson(dto);
         System.out.println(json);
         response.getWriter().write(json);
    }
}
