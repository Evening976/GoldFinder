package com.example.goldfinder.server.commands;

import com.example.utils.Player;
import com.example.goldfinder.server.GameServer;
import com.example.utils.gdGame;

public class Dir implements IServerCommand {
    Player player;
    gdGame game;

    @Override
    public String run(GameServer server, Player p, gdGame g, String[] params) {
        int xPosIncrement = 0;
        int yPosIncrement = 0;
        boolean collectGold = false;
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
                        game.collectGold(p);
                    }
                }
            }
            case "DOWN" -> {
                dir = game.getDown(p.getxPos(), p.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD")) {
                    game.movePlayer(p, 0, 1);
                    if (dir.contains("GOLD")) {
                        System.out.println("COLLECTED GOLD");
                        game.collectGold(p);
                    }
                }
            }
            case "LEFT" -> {
                dir = game.getLeft(p.getxPos(), p.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD")) {
                    game.movePlayer(p, -1, 0);
                    if (dir.contains("GOLD")) {
                        game.collectGold(p);
                    }
                }
            }
            case "RIGHT" -> {
                dir = game.getRight(p.getxPos(), p.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD")) {
                    game.movePlayer(p, 1, 0);
                    if (dir.contains("GOLD")) {
                        game.collectGold(p);
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
    public Player getPlayer() {
        return game.getPlayer(player);
    }
}
