package com.example.utils.games;

import com.example.goldfinder.server.ScoreManager;
import com.example.utils.players.AbstractPlayer;
import javafx.util.Pair;

import java.util.*;

public class GameMap {
    Map<Short /*Game ID*/, AbstractGame> games = new HashMap<>();
    TreeMap<Integer, ArrayList<String>> scores = new TreeMap<>();
    int maxPlayers = 4;
    int maxGames = 10;

    public GameMap(int maxPlayers, int maxGames) {
        this.maxPlayers = maxPlayers;
        this.maxGames = maxGames;
        games = new HashMap<>(maxGames);
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
        System.out.println(games.size());
        for (Short key : games.keySet()) {
            if(games.get(key).hasEnded()){
                games.remove(key);
                continue;
            }
            if (!games.get(key).isRunning() && games.get(key).isSolo() == solo && game.getClass() == games.get(key).getClass()) {
                return new Pair<>(key, games.get(key));
            }
        }
        Short key = (short) games.size();
        if (solo) {
            games.put(key, new GFGame());
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

    public void setGameEnded(short gameID) {
        games.remove(gameID);
    }

    public void setGame(short gameID, AbstractGame game) {
        games.put(gameID, game);
    }


    public int getRunningGames() {
        int count = 0;
        for (Short key : games.keySet()) {
            if (games.get(key).isRunning()) {
                count++;
            }
        }
        return count;
    }

    public TreeMap<Integer, ArrayList<String>> saveScores(short gameID) {
        scores.clear();
        for(AbstractPlayer p : games.get(gameID).getPlayers()){
            ScoreManager.addToLeaderboards(scores, p.getScore(), p.getName());
        }
        return scores;
    }
}
