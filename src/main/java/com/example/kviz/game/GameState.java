package com.example.kviz.game;

import com.example.kviz.model.Question;
import com.example.kviz.model.Quiz;
import com.example.kviz.model.supporting.QuestionDTO;
import com.example.kviz.webSocket.UserData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.websocket.Session;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameState {
    Quiz quiz;
    int currentQuestionIndex;
    private Session admin;
    private final Map<Session, UserData> players = new ConcurrentHashMap<>();
    private final String gameId;
    private long deadline;

    private final Gson gson = new GsonBuilder().create();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public GameState(String gameId, Quiz quiz) {
        this.quiz = quiz;
        this.gameId = gameId;
        currentQuestionIndex = -1;
    }

    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
    public Quiz getQuiz() { return this.quiz; }

    public void setAdmin(Session admin) { this.admin = admin; }
    public Session getAdmin() { return admin; }

    public void addPlayer(Session p) {players.put(p, new UserData());}
    public void editName(Session p, String name){ players.get(p).setName(name);}
    public void removePlayer(Session p) { players.remove(p); }
    public Set<Session> getPlayers() { return players.keySet(); }

    public Question nextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex >= quiz.getQuestions().size()) {
            currentQuestionIndex = 0;
            return null;
        }else{
            deadline = System.currentTimeMillis() + quiz.getQuestions().get(currentQuestionIndex).getTime() * 1000L;
            broadcastQuestionAndDeadline();
            return quiz.getQuestions().get(currentQuestionIndex);
        }
    }

    public long getDeadline() { return deadline; }

    public boolean handleAnswer(Session player, String answer){
        Question q = quiz.getQuestions().get(currentQuestionIndex);
        boolean correct = q.checkIfCorrectAnswer(answer);
        if (correct) {
            players.get(player).addPoints(q.getPoints());
        }
        return correct;
    }
    public void handleAnswer(Session player, List<String> answers){}

    private void broadcastQuestionAndDeadline(){
        Question q = quiz.getQuestions().get(currentQuestionIndex);
        String question = q.getQuestion();
        String answers = gson.toJson(q.getAnswers());
        String time = q.getTime().toString();
        String deadlineString = gson.toJson(deadline);
        String payload = "{\"type\":\"question\", \"question\":\"" + question + "\", \"answers\":" + answers + ", \"time\":\"" + time + "\", \"deadline\":\"" + deadlineString + "\"}";
        System.out.println(payload);
        try {
            admin.getBasicRemote().sendText(payload);
            for (Session s : players.keySet()) {
                System.out.println("sending to player: " + s);
                s.getBasicRemote().sendText(payload);
            }
            scheduler.schedule(this::endQuestion,q.getTime() , TimeUnit.SECONDS);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void endQuestion(){
        System.out.println("end question");
        broadcastLeaderboard();
    }

    public String serializeLeaderboard() {
        List<UserData> sorted = new ArrayList<>(players.values());
        sorted.sort((a, b) -> Integer.compare(b.getPoints(), a.getPoints()));

        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("type", "endQuestion");
        wrapper.put("players", sorted);

        System.out.println(gson.toJson(wrapper));
        return gson.toJson(wrapper);
    }

    public void broadcastLeaderboard() {
        String json = serializeLeaderboard();
        try{
           admin.getBasicRemote().sendText(json);
        }catch (IOException e){
            e.printStackTrace();
        }
        for (Session s : players.keySet()) {
            try {
                s.getBasicRemote().sendText(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getGameId() { return gameId; }
}
