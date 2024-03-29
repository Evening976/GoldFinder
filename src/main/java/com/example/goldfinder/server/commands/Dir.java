package com.example.goldfinder.server.commands;

import com.example.goldfinder.client.commands.Game_End;
import com.example.goldfinder.server.GameServer;
import com.example.utils.games.*;
import com.example.utils.players.AbstractPlayer;
import com.example.utils.players.GFPlayer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;

public class Dir implements IServerCommand {
    AbstractPlayer player;
    AbstractGame game;
    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame g, InetSocketAddress addr, String[] params) {
        this.player = p;
        this.game = g;
        String dir = "INVALID_MOVE";

        switch (params[0].toUpperCase()) {
            case "UP" -> {
                dir = game.getUp(p.getxPos(), p.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD")) {
                    game.movePlayer(p, 0, -1);
                    handleGFGame(server, p, g, dir);
                }
            }
            case "DOWN" -> {
                dir = game.getDown(p.getxPos(), p.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD")) {
                    game.movePlayer(p, 0, 1);
                    handleGFGame(server, p, g, dir);
                }
            }
            case "LEFT" -> {
                dir = game.getLeft(p.getxPos(), p.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD")) {
                    game.movePlayer(p, -1, 0);
                    handleGFGame(server, p, g, dir);
                }
            }
            case "RIGHT" -> {
                dir = game.getRight(p.getxPos(), p.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD")) {
                    game.movePlayer(p, 1, 0);
                    handleGFGame(server ,p, g, dir);
                }
            }
        }


        if(game.hasEnded()){
            return "GAME_END";
        }
        System.out.println(dir);
        if (!dir.endsWith("WALL ") && !dir.contains("PLAYER")) {
            System.out.println("VALID_MOVE:" + dir.stripTrailing().replace(dir.split(":")[0] + ": ", ""));
            return "VALID_MOVE:" + dir.stripTrailing().replace(dir.split(":")[0] + ": ", "");
        }
        System.out.println("INVALID_MOVE");
        return "INVALID_MOVE";
    }

    private void handleGFGame(GameServer server,AbstractPlayer p, AbstractGame g,String dir) {
        System.out.println(p.getxPos() + " " + p.getyPos() + " " + dir);
        System.out.println(g.getPlayer(p).getxPos() + " " + g.getPlayer(p).getyPos() + " " + dir);
        if(game instanceof GFGame || p instanceof GFPlayer) {
            ((GFGame)game).setDiscoveredCell(p.getxPos(), p.getyPos());
            if (dir.contains("GOLD")) {
                game.collectGold((GFPlayer) p);
            }
            if(((GFGame)game).getMaxCells() == ((GFGame)game).getDiscoveredCells()) {
                for(AbstractPlayer abstractPlayer : game.getPlayers()) {
                    System.out.println("Game ended");
                    server.sendMessage(abstractPlayer.getClient(),
                            "GAME_END", abstractPlayer.getAddress());
                    game.setHasEnded(true);
                }
            }
        }
    }

    @Override
    public AbstractGame getGame() {
        return game;
    }

    @Override
    public AbstractPlayer getPlayer() {
        return game.getPlayer(player);
    }
}
