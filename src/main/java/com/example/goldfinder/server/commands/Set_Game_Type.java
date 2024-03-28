package com.example.goldfinder.server.commands;

import com.example.goldfinder.server.GameServer;
import com.example.utils.games.gdGame;
import com.example.utils.players.AbstractPlayer;

import java.nio.channels.SelectableChannel;

public class Set_Game_Type implements IServerCommand{
    AbstractPlayer _player;
    gdGame _game;
    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer p, gdGame g, String[] params) {
        return null;
        //TO DO


    }

    @Override
    public gdGame getGame() {
        return null;
        //TO DO
    }

    @Override
    public AbstractPlayer getPlayer() {
        return null;
        //TO DO
    }
}
