package com.example.goldfinder.server.commands;

import com.example.utils.Players.AbstractPlayer;
import com.example.goldfinder.server.GameServer;
import com.example.utils.Games.gdGame;

import java.nio.channels.SelectableChannel;

public interface IServerCommand {
    String run(SelectableChannel client, GameServer server, AbstractPlayer p, gdGame g, String[] params);
    gdGame getGame();
    AbstractPlayer getPlayer();
}
