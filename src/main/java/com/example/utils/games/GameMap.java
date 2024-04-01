package com.example.utils.games;

import com.example.goldfinder.server.ScoreManager;
import com.example.utils.players.AbstractPlayer;
import javafx.util.Pair;

import java.util.*;

import static com.example.goldfinder.server.DispatcherServer.DEFAULT_PLAYER_COUNT;

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

    public Pair<Short, AbstractGame> getAvailable(AbstractGame game, int maxPlayers) {
        for (Short key : games.keySet()) {
            if (games.get(key).hasEnded()) {
                games.remove(key);
                continue;
            }
            if (!games.get(key).isRunning() && game.getClass() == games.get(key).getClass()) {
                if(maxPlayers != -1){
                    if(games.get(key).maxPlayers != maxPlayers){
                        continue;
                    }
                }
                return new Pair<>(key, games.get(key));
            }
        }
        Short key = (short) games.size();
        maxPlayers = maxPlayers == -1 ? DEFAULT_PLAYER_COUNT : maxPlayers;
        if (game instanceof GFGame) {
            games.put(key, new GFGame(maxPlayers));
        } else if (game instanceof CRGame) {
            games.put(key, new CRGame(maxPlayers));
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
        for (AbstractPlayer p : games.get(gameID).getPlayers()) {
            ScoreManager.addToLeaderboards(scores, p.getScore(), p.getName());
        }
        return scores;
    }
}
