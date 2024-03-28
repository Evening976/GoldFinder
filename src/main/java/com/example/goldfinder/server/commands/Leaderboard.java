package com.example.goldfinder.server.commands;

import com.example.utils.players.AbstractPlayer;
import com.example.goldfinder.server.GameServer;
import com.example.utils.games.gdGame;

import java.nio.channels.SelectableChannel;

public class Leaderboard implements IServerCommand {
    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer p, gdGame g, String[] params) {
        return null;
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
