package com.example.goldfinder.server.commands;

import com.example.goldfinder.server.GameServer;
import com.example.utils.games.AbstractGame;
import com.example.utils.games.GFGame;
import com.example.utils.players.AbstractPlayer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;

public class Set_Game_Type implements IServerCommand{
    AbstractPlayer _player;
    GFGame _game;
    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame g, InetSocketAddress addr, String[] params) {
        return null;
        //TO DO


    }

    @Override
    public AbstractGame getGame() {
        return null;
        //TO DO
    }

    @Override
    public AbstractPlayer getPlayer() {
        return null;
        //TO DO
    }
}
