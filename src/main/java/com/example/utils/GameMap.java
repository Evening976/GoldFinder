package com.example.utils;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class GameMap {
    Map<Pair<Boolean /*isRunning*/, Short /*Game ID*/>, gdGame> games = new HashMap<>();
    int maxPlayers = 4;
    int maxGames = 10;

    public GameMap() {
        games.put(new Pair<>(true, (short) 0), new gdGame(4));
    }

    public GameMap(int maxPlayers, int maxGames) {
        this.maxPlayers = maxPlayers;
        this.maxGames = maxGames;
        new GameMap();
    }

    public gdGame getByID(short gameID) {
        for (Pair<Boolean, Short> key : games.keySet()) {
            if (key.getValue() == gameID) {
                return games.get(key);
            }
        }
        return null;
    }

    public Pair<Short, gdGame> getAvailable() {
        for (Pair<Boolean, Short> key : games.keySet()){
            if (key.getKey() && !games.get(key).isReady()) {
                return new Pair<>(key.getValue(), games.get(key));
            }
        }
        Pair<Boolean, Short> key = new Pair<>(false, (short) games.size());
        games.put(key, new gdGame(maxPlayers));
        return new Pair<>(key.getValue(), games.get(key));
    }
}
