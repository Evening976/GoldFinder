package com.example.goldfinder.server.commands;

import com.example.utils.players.AbstractPlayer;
import com.example.goldfinder.server.GameServer;
import com.example.utils.games.gdGame;
import com.example.utils.players.GFPlayer;

import java.nio.channels.SelectableChannel;

public class Dir implements IServerCommand {
    AbstractPlayer player;
    gdGame game;

    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer p, gdGame g, String[] params) {
        this.player = p;
        this.game = g;
        String dir = "INVALID_MOVE";

        switch (params[0].toUpperCase()) {
            case "UP" -> {
                dir = game.getUp(p.getxPos(), p.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD")) {
                    game.movePlayer(p, 0, -1);
                    if (dir.contains("GOLD")) {
                        System.out.println("COLLECTED GOLD");
                        game.collectGold((GFPlayer) p);
                    }
                }
            }
            case "DOWN" -> {
                dir = game.getDown(p.getxPos(), p.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD")) {
                    game.movePlayer(p, 0, 1);
                    if (dir.contains("GOLD")) {
                        System.out.println("COLLECTED GOLD");
                        game.collectGold((GFPlayer) p);
                    }
                }
            }
            case "LEFT" -> {
                dir = game.getLeft(p.getxPos(), p.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD")) {
                    game.movePlayer(p, -1, 0);
                    if (dir.contains("GOLD")) {
                        game.collectGold((GFPlayer) p);
                    }
                }
            }
            case "RIGHT" -> {
                dir = game.getRight(p.getxPos(), p.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD")) {
                    game.movePlayer(p, 1, 0);
                    if (dir.contains("GOLD")) {
                        game.collectGold((GFPlayer) p);
                    }
                }
            }
        }
        ;

        System.out.println(dir);

        if (!dir.endsWith("WALL ") && !dir.contains("PLAYER")) {
            System.out.println("VALID_MOVE:" + dir.stripTrailing().replace(dir.split(":")[0] + ": ", ""));
            return "VALID_MOVE:" + dir.stripTrailing().replace(dir.split(":")[0] + ": ", "");
        }
        System.out.println("INVALID_MOVE");
        return "INVALID_MOVE";
    }

    @Override
    public gdGame getGame() {
        return game;
    }

    @Override
    public AbstractPlayer getPlayer() {
        return game.getPlayer(player);
    }
}
