package com.example.utils.games;

import com.example.utils.players.GFPlayer;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class GameMap {
    Map<Short /*Game ID*/, gdGame> games = new HashMap<>();
    int maxPlayers = 4;
    int maxGames = 10;

    public GameMap() {
        games.put((short) 0, new gdGame(maxPlayers));
    }

    public GameMap(int maxPlayers, int maxGames) {
        this.maxPlayers = maxPlayers;
        this.maxGames = maxGames;
        games.put((short) 0, new gdGame(maxPlayers));
    }

    public gdGame getByID(short gameID) {
        for (Short id : games.keySet()) {
            if (id == gameID) {
                return games.get(id);
            }
        }
        return null;
    }

    public Pair<Short, gdGame> getAvailable() {
        for (Short key : games.keySet()){
            if (!games.get(key).isRunning()) {
                return new Pair<>(key, games.get(key));
            }
        }

        Short key = (short)games.size();
        games.put(key, new gdGame(maxPlayers));
        System.out.println("Game created with ID " + key);
        //if(games.size() >= maxGames) return null;
        return new Pair<>(key, games.get(key));
    }

    public void setGame(short gameID, gdGame game) {
        games.put(gameID, game);
    }

    public void removePlayer(GFPlayer p) {
        System.out.println("Removing player " + p.getName());
        for (Short key : games.keySet()) {
            gdGame game = games.get(key);
            if (game.getPlayers().contains(p)) {
                game.removePlayer(p);
                if (game.getPlayers().isEmpty()) {
                    games.remove(key);
                }
                return;
            }
        }
    }
}
