package com.example.goldfinder.server.commands;

import com.example.goldfinder.server.GameServer;
import com.example.utils.Players.AbstractPlayer;
import com.example.utils.Players.GFPlayer;
import com.example.utils.Games.gdGame;

import java.nio.channels.SelectableChannel;

public class Game_Start implements IServerCommand{
    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer p, gdGame g, String[] params) {
        StringBuilder sb = new StringBuilder();
        sb.append("GAME_START ");
        for(AbstractPlayer player : g.getPlayers()){
            sb.append(player.getName()).append(":").append(g.getPlayers().indexOf(player)).append(" ");
        }
        return sb.toString();
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
