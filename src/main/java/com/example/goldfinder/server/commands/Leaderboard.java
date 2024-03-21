package com.example.goldfinder.server.commands;

import com.example.goldfinder.Player;
import com.example.goldfinder.server.GameServer;
import com.example.utils.gdGame;

public class Leaderboard implements IServerCommand {
    @Override
    public String run(GameServer server, Player p, gdGame g, String[] params) {
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
