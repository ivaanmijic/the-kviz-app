package com.example.kviz.game;

import com.example.kviz.model.Question;
import com.example.kviz.model.Quiz;
import com.example.kviz.model.supporting.QuestionDTO;
import com.example.kviz.webSocket.UserData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GameState {
    Quiz quiz;
    int currentQuestionIndex;
    private Session admin;
    private final Map<Session, UserData> players = new ConcurrentHashMap<>();
    private final String gameId;
    private long deadline;
    private boolean started;

    public boolean isStarted() {
        return started;
    }
    public void setStarted(boolean started) {
        this.started = started;
    }

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
    public Set<Session> removeAllNoNamePlayers(){
        Set<Session> toRemove = ConcurrentHashMap.newKeySet();
        players.forEach((p, u) -> {
            if(u.getName() == null || u.getName().isBlank()){
                toRemove.add(p);
            }
        });
        toRemove.forEach(players::remove);
        return toRemove;
    };

    public Question nextQuestion() {
        if(currentQuestionIndex == -1) {
            Set<Session> toRemove = removeAllNoNamePlayers();
            toRemove.forEach((p)->{
                try {
                    p.close(new CloseReason(
                            CloseReason.CloseCodes.GOING_AWAY,
                            "Game ended because game has already started"));
                } catch (IOException ignored) {}
            });
            setStarted(true);
        }
        currentQuestionIndex++;
        System.out.println(quiz.getQuestions().size() + " " + currentQuestionIndex);
        if (currentQuestionIndex >= quiz.getQuestions().size()) {
            System.out.println("No more questions in this game");
            currentQuestionIndex = 0;
            finalLeaderboard();
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
    public boolean handleAnswer(Session player, JsonArray answers){
        Question q = quiz.getQuestions().get(currentQuestionIndex);
        List<String> list = StreamSupport.stream(answers.spliterator(), false)
                .map(JsonElement::getAsString)
                .collect(Collectors.toList());
        for (String s : list) {
           System.out.println(s + " " + players.get(player).getName());
        }
        double percent = q.checkIfCorrectAnswers(list);
        if (percent != 0) {
            players.get(player).addPoints((int)(q.getPoints()*percent));
            return true;
        }
        return false;
    }

    private void broadcastQuestionAndDeadline(){
        Question q = quiz.getQuestions().get(currentQuestionIndex);
        String questionType = q.getType().toString().toLowerCase();
        String question = q.getQuestion();
        System.out.println("Question: " + question);
        String answers = gson.toJson(q.getAnswersAsString());
        System.out.println("Answers: " + answers);
        String time = q.getTime().toString();
        String deadlineString = gson.toJson(deadline);
        String payload = "{\"type\":\"question\",\"questionType\":\"" + questionType +"\", \"question\":\"" + question + "\", \"answers\":" + answers + ", \"time\":\"" + time + "\", \"deadline\":\"" + deadlineString + "\"}";
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

    private void finalLeaderboard(){
        broadcastLeaderboard("finalLeaderboard");
    }

    private void endQuestion(){
        System.out.println("end question");
        broadcastLeaderboard("endQuestion");
    }

    public String serializeLeaderboard(String type) {
        List<UserData> sorted = new ArrayList<>(players.values());
        sorted.sort((a, b) -> Integer.compare(b.getPoints(), a.getPoints()));

        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("type", type);
        wrapper.put("players", sorted);

        System.out.println(gson.toJson(wrapper));
        return gson.toJson(wrapper);
    }

    public void broadcastLeaderboard(String type) {
        String json = serializeLeaderboard(type);
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
