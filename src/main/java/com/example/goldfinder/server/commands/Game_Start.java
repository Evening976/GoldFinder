package com.example.goldfinder.server.commands;

import com.example.goldfinder.server.GameServer;
import com.example.utils.Player;
import com.example.utils.gdGame;

import java.nio.channels.SelectableChannel;

public class Game_Start implements IServerCommand{
    @Override
    public String run(SelectableChannel client, GameServer server, Player p, gdGame g, String[] params) {
        StringBuilder sb = new StringBuilder();
        sb.append("GAME_START ");
        for(Player player : g.getPlayers()){
            sb.append(player.getName()).append(":").append(g.getPlayers().indexOf(player)).append(" ");
        }
        return sb.toString();
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
