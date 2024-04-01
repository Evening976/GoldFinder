package com.example.goldfinder.server.commands.gameserver;

import com.example.goldfinder.server.GameServer;
import com.example.utils.games.AbstractGame;
import com.example.utils.players.AbstractPlayer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;

public class Game_End implements GameServerCommand {
    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame g, InetSocketAddress addr, String[] params) {
        server.saveScore(server.getGames().saveScores(p.getGameID()));

        StringBuilder response = new StringBuilder();
        response.append("GAME_END ");
        for(AbstractPlayer player : g.getPlayers()) {
            response.append(player.getName()).append(":").append(player.getScore()).append(" ");
        }
        response.append("END");
        return response.toString();
    }

    @Override
    public AbstractGame getGame() {
        return null;
    }

    @Override
    public AbstractPlayer getPlayer() {
        return null;
    }
}
