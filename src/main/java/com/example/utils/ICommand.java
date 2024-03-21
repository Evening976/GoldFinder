package com.example.utils;

import com.example.goldfinder.Player;
import com.example.goldfinder.server.GameServer;

public interface ICommand {
    String run(GameServer server, Player p, gdGame g, String[] params);
    gdGame getGame();
    Player getPlayer();
}
