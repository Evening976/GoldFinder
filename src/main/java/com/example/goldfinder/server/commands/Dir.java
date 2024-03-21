package com.example.goldfinder.server.commands;

import com.example.utils.Player;
import com.example.goldfinder.server.GameServer;
import com.example.utils.gdGame;

public class Dir implements IServerCommand {
    Player player;
    gdGame game;

    @Override
    public String run(GameServer server, Player p, gdGame g, String[] params) {
        this.player = p;
        this.game = g;
        String dir = "INVALID_MOVE";
        switch (params[0].toUpperCase()) {
            case "UP" -> {
                dir = game.getUp(p.getxPos(), p.getyPos());
                if(dir.contains("VALID")){
                    if(dir.contains("GOLD"))
                        p.collectGold();
                    game.movePlayer(p, 0, -1);
                }
            }
            case "DOWN" -> {
                dir = game.getDown(p.getxPos(), p.getyPos());
                if(dir.contains("VALID")){
                    if(dir.contains("GOLD"))
                        p.collectGold();
                    game.movePlayer(p, 0, 1);
                }
            }
            case "LEFT" ->  {
                dir = game.getLeft(p.getxPos(), p.getyPos());
                if(dir.contains("VALID")){
                    if(dir.contains("GOLD"))
                        p.collectGold();
                    game.movePlayer(p, -1, 0);
                }
            }
            case "RIGHT" -> {
                dir = game.getRight(p.getxPos(), p.getyPos());
                if(dir.contains("VALID")){
                    if(dir.contains("GOLD"))
                        p.collectGold();
                    game.movePlayer(p, 1, 0);
                }
            }
        };

        if (!dir.endsWith("WALL ") && !dir.contains("PLAYER")){
            System.out.println("VALID_MOVE:" + dir.stripTrailing().replace(dir.split(":")[0] + ": ", ""));
            return "VALID_MOVE:" + dir.stripTrailing().replace(dir.split(":")[0] + ": ", "");}
        System.out.println("INVALID_MOVE");
        return "INVALID_MOVE";
    }

    @Override
    public gdGame getGame() {
        return game;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
