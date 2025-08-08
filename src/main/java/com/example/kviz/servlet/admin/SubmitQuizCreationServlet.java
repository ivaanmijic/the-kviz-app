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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        String thumbnailPath = saveImageToDisk(quizImagePart, "quizImages", "quizImage" + quiz.getId(), request);
        quiz.setThumbnail(thumbnailPath);

        List<Question> questions = new ArrayList<>();
        int questionIndex = 0;
        while(true){
            System.out.println("jodlejodlejodlejodelIOOOOOOO");
            QuestionServices questionServices = new QuestionServices();
            Question question = new Question();
            String title = request.getParameter("questions[" + questionIndex + "][title]");
            if(title == null){
                break;
            }
            question.setQuestion(title);
            question.setType(QuestionTypeFactory.getType(request.getParameter("questions[" + questionIndex + "][category]").toLowerCase()));
            question.setPoints(Integer.parseInt(request.getParameter("questions[" + questionIndex + "][points]")));
            question.setTime(Integer.parseInt(request.getParameter("questions[" + questionIndex + "][time]")));
            question.setQuiz(quiz);

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
            String questionImage = saveImageToDisk(questionImagePart, "questions", "quiz" + quiz.getId() + "_" + "question" + question.getId(), request);
            question.setImage(questionImage);
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

    private String saveImageToDisk(Part part, String folder, String filenameBase, HttpServletRequest request) throws IOException {
        if (part == null || part.getSize() == 0) return null;

        String uploadsReal = getServletContext().getRealPath("/uploads/" + folder);
        if (uploadsReal == null) {
            File tmp = (File) getServletContext().getAttribute("javax.servlet.context.tempdir");
            uploadsReal = new File(tmp, "uploads/" + folder).getAbsolutePath();
        }
        Path uploadsPath = Paths.get(uploadsReal);
        Files.createDirectories(uploadsPath);

        String submitted = part.getSubmittedFileName();
        String ext = ".jpg";
        if (submitted != null && submitted.contains(".")) {
            ext = submitted.substring(submitted.lastIndexOf('.'));
        }

        String filename = filenameBase + ext;
        Path filePath = uploadsPath.resolve(filename);

        try (InputStream in = part.getInputStream()) {
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "uploads/" + folder + "/" + filename;
    }
}
