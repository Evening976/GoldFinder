package com.example.goldfinder.server.commands;

import com.example.utils.Player;
import com.example.goldfinder.server.GameServer;
import com.example.utils.gdGame;

public class Surrounding implements IServerCommand {

    Player player;
    gdGame game;

    @Override
    public String run(GameServer server, Player p, gdGame g, String[] params) {
        for(String s : params) System.out.println(s);
        this.player = p;
        this.game = g;
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
