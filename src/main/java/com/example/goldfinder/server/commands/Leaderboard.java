package com.example.goldfinder.server.commands;

import com.example.utils.Player;
import com.example.goldfinder.server.GameServer;
import com.example.utils.gdGame;

import java.nio.channels.SelectableChannel;

public class Leaderboard implements IServerCommand {
    @Override
    public String run(SelectableChannel client, GameServer server, Player p, gdGame g, String[] params) {
        return null;
    }

    @Override
    public gdGame getGame() {
        return null;
    }

    @Override
    public Player getPlayer() {
        return null;
    }
}
