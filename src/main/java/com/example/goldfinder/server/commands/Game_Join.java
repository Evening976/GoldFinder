package com.example.goldfinder.server.commands;

import com.example.utils.Player;
import com.example.goldfinder.server.GameServer;
import com.example.utils.Logger;
import com.example.utils.gdGame;
import javafx.util.Pair;

public class Game_Join implements IServerCommand {
    Player player;
    gdGame game;
    @Override
    public String run(GameServer server, Player player, gdGame game, String[] params) {
        this.player = player;

        String playerName = params[1];
        player.setName(playerName);
        Pair<Short, gdGame> availableGame = server.getGames().getAvailable();
        player.attachToGame(availableGame.getKey());
        availableGame.getValue().addPlayer(this.player);
        availableGame.getValue().spawnPlayer(player);

        this.game = availableGame.getValue();

        return Logger.getDebugLog("Player " + playerName + " joined game " + availableGame.getKey());
    }

    @Override
    public gdGame getGame() {
        return game;
    }

    @Override
    public Player getPlayer() {
        return game.getPlayer(player);
    }
}
