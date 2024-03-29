package com.example.goldfinder.server.commands;

import com.example.goldfinder.server.GameServer;
import com.example.utils.games.AbstractGame;
import com.example.utils.players.AbstractPlayer;

import java.nio.channels.SelectableChannel;

public interface IServerCommand {
    String run(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame g, String[] params);
    AbstractGame getGame();
    AbstractPlayer getPlayer();
}
