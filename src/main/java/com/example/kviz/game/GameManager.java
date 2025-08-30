package com.example.kviz.game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
    public static final Map<String, GameState> activeGames = new ConcurrentHashMap<>();
}
