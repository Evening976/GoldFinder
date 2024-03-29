package com.example.utils.games;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class GameMap {
    Map<Short /*Game ID*/, AbstractGame> games = new HashMap<>();
    int maxPlayers = 4;
    int maxGames = 10;

    public GameMap() {
        games.put((short) 0, new GFGame(maxPlayers));
    }

    public GameMap(int maxPlayers, int maxGames) {
        this.maxPlayers = maxPlayers;
        this.maxGames = maxGames;
        games.put((short) 0, new GFGame(maxPlayers));
    }

    public AbstractGame getByID(short gameID) {
        for (Short id : games.keySet()) {
            if (id == gameID) {
                return games.get(id);
            }
        }
        return null;
    }

    public Pair<Short, AbstractGame> getAvailable(AbstractGame game, boolean solo) {
        for (Short key : games.keySet()) {
            if (!games.get(key).isRunning() && games.get(key).isSolo() == solo && game.getClass() == games.get(key).getClass()) {
                return new Pair<>(key, games.get(key));
            }
        }
        Short key = (short) games.size();
        if (solo) {
            games.put(key, new GFGame());
            System.out.println("Solo");
        } else {
            if (game instanceof GFGame) {
                games.put(key, new GFGame(maxPlayers));
            } else if (game instanceof CRGame) {
                games.put(key, new CRGame(maxPlayers));
            }
        }

        System.out.println("Game created with ID " + key);
        return new Pair<>(key, games.get(key));
    }

    public void setGame(short gameID, AbstractGame game) {
        games.put(gameID, game);
    }

}
