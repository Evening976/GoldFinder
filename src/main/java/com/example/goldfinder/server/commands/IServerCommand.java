package com.example.goldfinder.server.commands;

import com.example.utils.Player;
import com.example.goldfinder.server.GameServer;
import com.example.utils.gdGame;

public interface IServerCommand {
    String run(GameServer server, Player p, gdGame g, String[] params);
    gdGame getGame();
    Player getPlayer();
}
