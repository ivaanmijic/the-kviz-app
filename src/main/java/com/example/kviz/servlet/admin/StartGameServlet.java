package com.example.kviz.servlet.admin;

import com.example.kviz.game.GameIdGenerator;
import com.example.kviz.game.GameManager;
import com.example.kviz.game.GameState;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/admin/startGame")
public class StartGameServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String gameId = GameIdGenerator.generateGameCode();
        GameState game = new GameState(gameId);
        GameManager.activeGames.put(gameId, game);

        req.setAttribute("gameId", gameId);
        req.setAttribute("joinedPlayers", 0);
        req.getRequestDispatcher("/WEB-INF/views/admin/gameLobby.jsp").forward(req, resp);
    }


}
