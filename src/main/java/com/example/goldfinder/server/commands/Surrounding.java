package com.example.goldfinder.server.commands;

import com.example.utils.Player;
import com.example.goldfinder.server.GameServer;
import com.example.utils.gdGame;

import java.nio.channels.SelectableChannel;

public class Surrounding implements IServerCommand {

    Player player;
    gdGame game;

    @Override
    public String run(SelectableChannel client, GameServer server, Player p, gdGame g, String[] params) {
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
    public Player getPlayer() {
        return player;
    }
}
