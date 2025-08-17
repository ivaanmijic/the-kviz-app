package com.example.kviz.servlet.admin;

import com.example.kviz.model.Question;
import com.example.kviz.model.Quiz;
import com.example.kviz.model.supporting.QuestionDTO;
import com.example.kviz.model.supporting.QuizDTO;
import com.example.kviz.service.QuestionServices;
import com.example.kviz.service.QuizService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/admin/questions-info")
public class QuestionInformationServlet extends HttpServlet {
    private QuestionServices questionService;
    private Gson gson;

    public void init() throws ServletException {
        super.init();
        questionService = new QuestionServices();
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
    }    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int quizId = Integer.parseInt(request.getParameter("id"));
        List<Question> questions = questionService.findByQuizId((long) quizId);
        List<QuestionDTO> dtos = new ArrayList<>();
        for(Question question : questions) {
            QuestionDTO dto = QuestionDTO.fromEntity(question);
            dtos.add(dto);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(dtos);
        System.out.println(json);
        response.getWriter().write(json);
    }
}
