package com.example.kviz.servlet.admin;

import com.example.kviz.game.GameIdGenerator;
import com.example.kviz.game.GameManager;
import com.example.kviz.game.GameState;
import com.example.kviz.model.Question;
import com.example.kviz.model.Quiz;
import com.example.kviz.service.QuestionServices;
import com.example.kviz.service.QuizService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/admin/startGame")
public class StartGameServlet extends HttpServlet {
    private final QuizService quizService = new QuizService();
    private final QuestionServices questionServices = new QuestionServices();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String gameId = GameIdGenerator.generateGameCode();
        String quizId = req.getParameter("quizId");
        Quiz quiz = quizService.findById((long)Integer.parseInt(quizId)).get();
        List<Question> questions = questionServices.findByQuizId((long)Integer.parseInt(quizId));
        quiz.setQuestions(questions);
        GameState game = new GameState(gameId, quiz);
        GameManager.activeGames.put(gameId, game);
        HttpSession session = req.getSession();
        session.setAttribute("gameId", gameId);

        req.setAttribute("gameId", gameId);
        req.setAttribute("joinedPlayers", 0);
        req.getRequestDispatcher("/WEB-INF/views/admin/gameLobby.jsp").forward(req, resp);
    }


}
