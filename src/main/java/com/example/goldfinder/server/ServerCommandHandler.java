package com.example.goldfinder.server;

import com.example.utils.Player;
import com.example.utils.gdGame;

public class ServerCommandHandler {
    public static String handleCommand(String command, gdGame game, Player player) {
        String pre = command.split(" ")[0].toUpperCase();
        switch (pre){
            case "SURROUNDING": return game.getSurrounding(player.getxPos(), player.getyPos());
            case "dir":
                String direction = command.split(" ")[1];
                switch (direction.toUpperCase()) {
                    case "UP": return game.getLeft(player.getxPos(), player.getyPos());
                    case "DOWN": return game.getDown(player.getxPos(), player.getyPos());
                    case "LEFT": return game.getLeft(player.getxPos(), player.getyPos());
                    case "RIGHT": return game.getRight(player.getxPos(), player.getyPos());
                    default: return "Direction: " + direction + " not recognized";
                }
            default: return "Command: " + command + " not recognized";
        }
    }
}
