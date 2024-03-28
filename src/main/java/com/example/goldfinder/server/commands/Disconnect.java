package com.example.goldfinder.server.commands;

import com.example.goldfinder.server.GameServer;
import com.example.utils.Players.AbstractPlayer;
import com.example.utils.Games.gdGame;

import java.nio.channels.SelectableChannel;

public class Disconnect implements IServerCommand{
    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer p, gdGame g, String[] params) {
        g.removePlayer(p);
        //server.
        return "DISCONNECTED";
    }

    @Override
    public gdGame getGame() {
        return null;
    }

    @Override
    public AbstractPlayer getPlayer() {
        return null;
    }
}
