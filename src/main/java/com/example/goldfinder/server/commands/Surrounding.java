package com.example.goldfinder.server.commands;

import com.example.utils.players.AbstractPlayer;
import com.example.goldfinder.server.GameServer;
import com.example.utils.games.gdGame;

import java.nio.channels.SelectableChannel;

public class Surrounding implements IServerCommand {

    AbstractPlayer player;
    gdGame game;

    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer p, gdGame g, String[] params) {
        this.player = p;
        this.game = g;
        System.out.println("Surrounding " + p + ": " + game.getSurrounding(p.getxPos(),p.getyPos()));
        return game.getSurrounding(p.getxPos(),p.getyPos());
    }

    @Override
    public gdGame getGame() {
        return game;
    }

    @Override
    public AbstractPlayer getPlayer() {
        return player;
    }
}
