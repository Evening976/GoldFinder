package com.example.goldfinder.server.commands;

import com.example.goldfinder.Player;
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
        Pair<Short, gdGame> p = server.getGames().getAvailable();
        player.attachToGame(p.getKey());
        p.getValue().addPlayer(this.player);

        this.game = p.getValue();

        return Logger.getDebugLog("Player " + playerName + " joined game " + p.getKey());
    }

    @Override
    public gdGame getGame() {
        return game;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
