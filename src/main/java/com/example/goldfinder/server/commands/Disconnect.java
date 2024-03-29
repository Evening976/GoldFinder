package com.example.goldfinder.server.commands;

import com.example.goldfinder.server.GameServer;
import com.example.utils.games.AbstractGame;
import com.example.utils.players.AbstractPlayer;
import com.example.utils.games.GFGame;

import java.nio.channels.SelectableChannel;

public class Disconnect implements IServerCommand{
    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame g, String[] params) {
        g.removePlayer(p);
        //server.
        return "DISCONNECTED";
    }

    @Override
    public GFGame getGame() {
        return null;
    }

    @Override
    public AbstractPlayer getPlayer() {
        return null;
    }
}
