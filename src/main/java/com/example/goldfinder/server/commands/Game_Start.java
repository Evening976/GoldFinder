package com.example.goldfinder.server.commands;

import com.example.goldfinder.server.GameServer;
import com.example.utils.games.AbstractGame;
import com.example.utils.players.AbstractPlayer;
import com.example.utils.games.GFGame;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;

public class Game_Start implements IServerCommand{
    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame g, InetSocketAddress addr, String[] params) {
        StringBuilder sb = new StringBuilder();
        sb.append("GAME_START ");
        for(AbstractPlayer player : g.getPlayers()){
            sb.append(player.getName()).append(":").append(g.getPlayers().indexOf(player)).append(" ");
        }
        return sb.toString();
    }

    @Override
    public GFGame getGame() {
        return null;
    }

    @Override
    public AbstractPlayer getPlayer() {
        return null;
    }
}
