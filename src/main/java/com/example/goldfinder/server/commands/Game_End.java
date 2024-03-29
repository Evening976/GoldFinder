package com.example.goldfinder.server.commands;

import com.example.goldfinder.server.GameServer;
import com.example.utils.games.AbstractGame;
import com.example.utils.players.AbstractPlayer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;

public class Game_End implements IServerCommand{
    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame g, InetSocketAddress addr, String[] params) {

        return "GAME_END";
    }

    @Override
    public AbstractGame getGame() {
        return null;
    }

    @Override
    public AbstractPlayer getPlayer() {
        return null;
    }
}
