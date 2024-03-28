package com.example.goldfinder.server.commands;

import com.example.goldfinder.server.GameServer;
import com.example.utils.games.gdGame;
import com.example.utils.Logger;
import com.example.utils.players.AbstractPlayer;
import javafx.util.Pair;

import java.nio.channels.SelectableChannel;

public class Game_Join implements IServerCommand {
    AbstractPlayer _player;
    gdGame _game;
    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer player, gdGame game, String[] params) {
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
            for(AbstractPlayer p : game.getPlayers()){
                if(p == player) continue;
                server.sendMessage(p.getClient(), new Game_Start().run(null, server, p, game, new String[]{}), p.getAddress());
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
    public AbstractPlayer getPlayer() {
        return _game.getPlayer(_player);
    }

    public String toString() {
        return "Game_Join";
    }
}
