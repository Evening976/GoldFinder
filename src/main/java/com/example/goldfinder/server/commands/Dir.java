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
        String dir = switch (params[0].toUpperCase()) {
            case "UP" -> game.getUp(p.getxPos(), p.getyPos());
            case "DOWN" -> game.getDown(p.getxPos(), p.getyPos());
            case "LEFT" ->  game.getLeft(p.getxPos(), p.getyPos());
            case "RIGHT" -> game.getRight(p.getxPos(), p.getyPos());
            default -> "INVALID_MOVE";
        };

        if (!dir.endsWith("WALL ") && !dir.contains("PLAYER"))
            return "VALID_MOVE:" + dir.stripTrailing().replace(dir.split(":")[0] + ": ", "");
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
