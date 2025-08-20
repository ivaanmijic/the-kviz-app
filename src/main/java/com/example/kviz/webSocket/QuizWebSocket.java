package com.example.kviz.webSocket;

import com.example.kviz.game.GameManager;
import com.example.kviz.game.GameState;
import com.google.gson.Gson;
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

        if ((game == null && "player".equals(role)) || (game!=null && game.isStarted())) {
            try {
                session.getBasicRemote().sendText("{\"type\":\"error\", \"message\":\"Invalid code\"}");
                session.close(new CloseReason(
                        CloseReason.CloseCodes.CANNOT_ACCEPT, "Game does not exist"));
            } catch (IOException ignored) {}
            return;
        }

        if ("admin".equals(role)) {
            if (game != null) game.setAdmin(session);
        } else if ("player".equals(role)) {
            game.addPlayer(session);
            try{
                session.getBasicRemote().sendText("{\"type\":\"codeOk\", \"gameId\":\"" + gameId + "\", \"quizTitle\":" + "\"" + game.getQuiz().getTitle() + "\"}");
            } catch (IOException ignored) {}

            Session admin = game.getAdmin();
            if (admin != null && admin.isOpen()) {
                try {
                    int playerCount = game.getPlayers().size();
                    admin.getBasicRemote().sendText(
                            "{\"type\":\"playerCount\",\"count\":" + playerCount + "}"
                    );
                    game.getPlayers().forEach(player -> {
                        try {
                            player.getBasicRemote().sendText("{\"type\":\"playerCount\",\"count\":" + playerCount + "}");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
            closeGame(gameId, game);
        } else if ("player".equals(role)) {
            game.removePlayer(session);
            // Optionally notify admin about player leaving
            notifyAllAboutLeaving(game);
        }
    }
    private void notifyAllAboutLeaving(GameState game){
        Session admin = game.getAdmin();
        if (admin != null && admin.isOpen()) {
            try {
                int playerCount = game.getPlayers().size();
                admin.getBasicRemote().sendText(
                        "{\"type\":\"playerCount\",\"count\":" + playerCount + "}"
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void closeGame(String gameId, GameState game) {
        // remove game first to prevent recursion issues
        GameManager.activeGames.remove(gameId);

        // close players safely
        for (Session p : game.getPlayers()) {
            if (p.isOpen()) {
                try {
                    p.close(new CloseReason(
                            CloseReason.CloseCodes.GOING_AWAY,
                            "Game ended because admin disconnected"));
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
                adminMessageHandler(message, session, game);
            } else if ("player".equals(role)) {
                playerMessageHandler(message, session, game);
                // Example: forward message from player to admin
                //Session admin = game.getAdmin();
                //if (admin != null && admin.isOpen()) {
                  //  admin.getBasicRemote().sendText(message);
                //}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playerMessageHandler(String message, Session session, GameState game) throws IOException {
        Gson gson = new Gson();
        System.out.println(message);
        JsonObject json = gson.fromJson(message, JsonObject.class);
        String type = json.get("type").getAsString();
        boolean correct;
        String msg;
        switch (type) {
            case "setUserName":
                game.editName(session, json.get("name").getAsString());
                System.out.println(json.get("name").getAsString());
                break;
            case "answer":
                correct = game.handleAnswer(session, json.get("answer").getAsString());
                if(correct){
                    msg = "{\"type\":\"answerReceived\", \"correctAnswer\": \"yes\"}";
                }else{
                    msg = "{\"type\":\"answerReceived\", \"correctAnswer\": \"no\"}";
                }
                try{
                    session.getBasicRemote().sendText(msg);
                }catch (IOException ignored) {}
                break;
            case "answers":
                correct = game.handleAnswer(session, json.get("answer").getAsJsonArray());
                if(correct){
                    msg = "{\"type\":\"answerReceived\", \"correctAnswer\": \"yes\"}";
                }else{
                    msg = "{\"type\":\"answerReceived\", \"correctAnswer\": \"no\"}";
                }
                try{
                    session.getBasicRemote().sendText(msg);
                }catch (IOException ignored) {}
                break;
        }
    }

    private void adminMessageHandler(String message, Session session, GameState game) throws IOException {
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(message, JsonObject.class);
        String type = json.get("type").getAsString();
        switch (type){
            case "nextQuestion":
                game.nextQuestion();
                break;
        }
    }
}


