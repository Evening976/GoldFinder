package com.example.goldfinder.server.commands.gameserver;

import com.example.goldfinder.server.GameServer;
import com.example.utils.GameUpdater;
import com.example.utils.games.AbstractGame;
import com.example.utils.players.AbstractPlayer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;

public class Dir implements GameServerCommand {
    AbstractPlayer player;
    AbstractGame game;
    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame g, InetSocketAddress addr, String[] params) {
        String dir = GameUpdater.updateGame(client, server, p, g, addr, params);
        player = p;
        game = g;
        return dir;
    }
    @Override
    public AbstractGame getGame() {
        return game;
    }

    @Override
    public AbstractPlayer getPlayer() { return game.getPlayer(player); }
}
