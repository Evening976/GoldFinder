package com.example.goldfinder.server.commands;

import com.example.utils.Player;
import com.example.goldfinder.server.GameServer;
import com.example.utils.Logger;
import com.example.utils.gdGame;
import javafx.util.Pair;

import java.nio.channels.SelectableChannel;

public class Game_Join implements IServerCommand {
    Player _player;
    gdGame _game;
    @Override
    public String run(SelectableChannel client, GameServer server, Player player, gdGame game, String[] params) {
        this._player = player;
        String playerName = params[1];
        player.setName(playerName);
        Pair<Short, gdGame> availableGame = server.getGames().getAvailable();
        if(availableGame.getValue() == null){
            return Logger.getDebugLog("No available games");
        }
        game = availableGame.getValue();
        game.addPlayer(player);
        player.attachToGame(availableGame.getKey(), (short) game.getPlayers().indexOf(player));

        if(game.isRunning()){
            for(Player p : game.getPlayers()){
                if(p == player) continue;
                server.sendMessage(p.getClient(), server.getrBuffer(), new Game_Start().run(null, server, p, game, new String[]{}));
            }
        }

        _game = game;
        _player = player;

        if(game.isRunning()){
            return new Game_Start().run(client, server, player, game, params);
        }
        return Logger.getDebugLog("Player " + playerName + " joined game " + availableGame.getKey());
    }

    @Override
    public gdGame getGame() {
        return _game;
    }

    @Override
    public Player getPlayer() {
        return _game.getPlayer(_player);
    }
}
