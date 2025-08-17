package com.example.kviz.servlet.admin;

import com.example.kviz.model.Admin;
import com.example.kviz.model.Question;
import com.example.kviz.model.Quiz;
import com.example.kviz.model.supporting.QuestionType;
import com.example.kviz.model.supporting.QuestionTypeFactory;
import com.example.kviz.model.supporting.QuizCategory;
import com.example.kviz.model.supporting.QuizCategoryFactory;
import com.example.kviz.service.QuestionServices;
import com.example.kviz.service.QuizService;
import com.mysql.cj.Session;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/admin/submit-quiz-creation")
@MultipartConfig(fileSizeThreshold = 1024*1024, maxFileSize = 10*1024*1024)
public class SubmitQuizCreationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getParts().forEach(part -> {
            System.out.println("Part: " + part.getName());
        });
        QuizService quizService = new QuizService();
        Quiz quiz = new Quiz();
        if(request.getParameter("quizId") != null) {
            int quizId = Integer.parseInt(request.getParameter("quizId"));
            quiz = quizService.findById((long) quizId).get();
        }
        HttpSession session = request.getSession();

        Admin owner = (Admin) session.getAttribute("admin");
        quiz.setOwner(owner);

        quiz.setTitle(request.getParameter("quizTitle"));
        String cat = request.getParameter("quizCategory");
        String visible =  request.getParameter("quizVisibility");

        quiz.setVisible(visible.equals("public"));

        quiz.setCategory(QuizCategoryFactory.getCategory(cat.toLowerCase()));

        quiz.setDescription(request.getParameter("quizDescription"));

        quiz = quizService.save(quiz);

        Part quizImagePart = request.getPart("quizImage");
        if(quizImagePart != null &&  quizImagePart.getSize() > 0) {
            saveImageToDisk(quizImagePart, "quizImages", "quizImage" + quiz.getId());
        }
        quiz.setThumbnail("quizImage" + quiz.getId() + ".jpg");

        List<Question> questions = new ArrayList<>();
        int questionIndex = 0;
        while(true){
            QuestionServices questionServices = new QuestionServices();
            Question question = new Question();
            if(request.getParameter("questions["+questionIndex + "][id]") != null) {
                int questionId = Integer.parseInt(request.getParameter("questions["+questionIndex + "][id]"));
                question = questionServices.findById((long) questionId).get();
            }
            String title = request.getParameter("questions[" + questionIndex + "][title]");
            if(title == null){
                break;
            }
            question.setQuestion(title);
            question.setType(QuestionTypeFactory.getType(request.getParameter("questions[" + questionIndex + "][category]").toLowerCase()));
            question.setPoints(Integer.parseInt(request.getParameter("questions[" + questionIndex + "][points]")));
            question.setTime(Integer.parseInt(request.getParameter("questions[" + questionIndex + "][time]")));
            question.setQuiz(quiz);
            question.setOrderNumber(questionIndex);

            List<String> answers = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                String answer = request.getParameter("questions[" + questionIndex + "][answers][" + i + "]");
                if (answer != null) answers.add(answer);
                System.out.println(answer);
            }

            question.setAnswers(answers);

            question.setCorrectAnswer(answers.get(Integer.parseInt(request.getParameter("questions[" + questionIndex + "][correctAnswer]"))));

            question = questionServices.save(question);

            Part questionImagePart = request.getPart("questions[" + questionIndex + "][image]");
            if(questionImagePart != null && questionImagePart.getSize() > 0) {
                saveImageToDisk(questionImagePart, "questions", "quiz" + quiz.getId() + "_" + "question" + question.getId());
            }
            question.setImage("quiz" + quiz.getId() + "_" + "question" + question.getId() + ".jpg");
            questionServices.save(question);

            questions.add(question);

            questionIndex++;
        }

        quiz.setQuestions(questions);
        quizService.save(quiz);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("{\"message\":\"ok\"}");
    }

    private String saveImageToDisk(Part part, String folder, String filename) throws IOException {
        if (part == null || part.getSize() == 0) return null;
        String uploads = getServletContext().getRealPath("uploads/" + folder);
        System.out.println("Resolved uploads path: " + uploads);

        Files.createDirectories(Paths.get(uploads));
        String filePath = uploads + "/" + filename + ".jpg";
        System.out.println("Final file path: " + filePath);

        part.write(filePath);
        return "uploads/" + folder + "/" + filename + ".jpg";
    }
}
