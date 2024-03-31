package com.example.goldfinder.server.commands.gameserver;

import com.example.goldfinder.server.GameServer;
import com.example.utils.Logger;
import com.example.utils.games.AbstractGame;
import com.example.utils.games.CRGame;
import com.example.utils.games.GFGame;
import com.example.utils.players.AbstractPlayer;
import com.example.utils.players.CRPlayer;
import com.example.utils.players.GFPlayer;
import javafx.util.Pair;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.util.Objects;

public class Game_Join implements GameServerCommand {
    AbstractPlayer _player;
    AbstractGame _game;

    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer player, AbstractGame game, InetSocketAddress addr, String[] params) {
        String playerName = params[1];
        Pair<Short, AbstractGame> availableGame;
        if (Objects.equals(params[2], "COPS_AND_ROBBERS")) {
            player = new CRPlayer(client, playerName, 0, 0);
            availableGame = server.getGames().getAvailable(new CRGame(2), false);
        } else {
            player = new GFPlayer(client, playerName, 0, 0);
            if (params[2] == null || !params[2].endsWith("_SOLO"))
                availableGame = server.getGames().getAvailable(new GFGame(), false);
            else availableGame = server.getGames().getAvailable(new GFGame(), true);
        }
        player.setAddress(addr);

        game = availableGame.getValue();
        game.addPlayer(player);
        player.attachToGame(availableGame.getKey(), (short) game.getPlayers().indexOf(player));

        _game = game;
        _player = player;


        if (game.isRunning()) {
            for (AbstractPlayer p : game.getPlayers()) {
                System.out.println(p);
                server.sendMessage(p.getClient(), new Game_Start().run(p.getClient(), server, p, game, (InetSocketAddress) p.getAddress(), new String[]{}), p.getAddress());
            }
        }

        return Logger.getBlue("Player " + playerName + " joined game " + availableGame.getKey());
    }

    @Override
    public AbstractGame getGame() {
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
