package com.example.kviz.webSocket;

import com.example.kviz.game.GameManager;
import com.example.kviz.game.GameState;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/quiz")
public class QuizWebSocket {
    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        Map<String, List<String>> params = session.getRequestParameterMap();
        String role = params.get("role").get(0);
        String gameId = params.get("gameId").get(0);

        GameState game = GameManager.activeGames.get(gameId);

        if (game == null && "player".equals(role)) {
            try {
                session.close(new CloseReason(
                        CloseReason.CloseCodes.CANNOT_ACCEPT, "Game does not exist"));
            } catch (IOException ignored) {}
            return;
        }

        if ("admin".equals(role)) {
            if (game != null) game.setAdmin(session);
        } else if ("player".equals(role)) {
            game.addPlayer(session);
        }
    }

    @OnClose
    public void onClose(Session session) {
        Map<String, List<String>> params = session.getRequestParameterMap();
        String gameId = params.get("gameId").get(0);
        String role = params.get("role").get(0);

        GameState game = GameManager.activeGames.get(gameId);
        if (game == null) return;

        if ("admin".equals(role)) {
            // If admin disconnects → close game & notify players
            for (Session p : game.getPlayers()) {
                try {
                    p.close(new CloseReason(
                            CloseReason.CloseCodes.GOING_AWAY,
                            "Game ended because admin disconnected"));
                } catch (IOException ignored) {}
            }
            GameManager.activeGames.remove(gameId);
        } else if ("player".equals(role)) {
            game.removePlayer(session);
            // Optionally notify admin about player leaving
            Session admin = game.getAdmin();
            if (admin != null && admin.isOpen()) {
                try {
                    admin.getBasicRemote().sendText("Player left");
                } catch (IOException ignored) {}
            }
        }
    }
    @OnMessage
    public void onMessage(String message, Session session) {
        Map<String, List<String>> params = session.getRequestParameterMap();
        String gameId = params.get("gameId").get(0);
        String role = params.get("role").get(0);

        GameState game = GameManager.activeGames.get(gameId);
        if (game == null) return; // game finished or invalid

        try {
            if ("admin".equals(role)) {
                // Example: forward message from admin to all players
                for (Session p : game.getPlayers()) {
                    if (p.isOpen()) p.getBasicRemote().sendText(message);
                }
            } else if ("player".equals(role)) {
                // Example: forward message from player to admin
                Session admin = game.getAdmin();
                if (admin != null && admin.isOpen()) {
                    admin.getBasicRemote().sendText(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
