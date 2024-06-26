package com.example.goldfinder.server.commands.gameserver;

import com.example.goldfinder.server.GameServer;
import com.example.utils.games.AbstractGame;
import com.example.utils.players.AbstractPlayer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;

public class Surrounding implements GameServerCommand {
    AbstractPlayer player;
    AbstractGame game;

    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame g, InetSocketAddress addr, String[] params) {
        this.player = p;
        this.game = g;
        return game.getSurrounding(p.getxPos(),p.getyPos());
    }

    @Override
    public AbstractGame getGame() {
        return game;
    }

    @Override
    public AbstractPlayer getPlayer() {
        return player;
    }
}
