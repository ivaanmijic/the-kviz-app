package com.example.kviz.game;

import jakarta.websocket.Session;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GameState {
    private Session admin;
    private final Set<Session> players = ConcurrentHashMap.newKeySet();
    private final String gameId;

    public GameState(String gameId) { this.gameId = gameId; }

    public void setAdmin(Session admin) { this.admin = admin; }
    public Session getAdmin() { return admin; }

    public void addPlayer(Session p) { players.add(p); }
    public void removePlayer(Session p) { players.remove(p); }
    public Set<Session> getPlayers() { return players; }

    public String getGameId() { return gameId; }
}
