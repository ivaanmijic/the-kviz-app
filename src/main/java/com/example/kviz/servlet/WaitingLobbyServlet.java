package com.example.kviz.servlet;

import com.example.kviz.game.GameManager;
import com.example.kviz.game.GameState;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/waitingLobby")
public class WaitingLobbyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String gameId = req.getParameter("gameId");
        if(gameId != null){
            GameState game = GameManager.activeGames.get(req.getParameter("gameId"));
            req.setAttribute("playerNumber", game.getPlayers().size());
        }
        req.getRequestDispatcher("/WEB-INF/views/player/waitingLobby.jsp").forward(req, resp);
    }
}
