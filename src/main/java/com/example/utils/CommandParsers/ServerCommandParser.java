package com.example.utils.CommandParsers;

import com.example.goldfinder.server.commands.*;

public class ServerCommandParser {
    public static IServerCommand parseCommand(String command) {
        String prefix = command.split(" ")[0];
        return switch (prefix.toUpperCase()) {
            case "GAME_JOIN" -> new Game_Join();
            case "SURROUNDING" -> new Surrounding();
            case "UP", "DOWN", "LEFT", "RIGHT" -> new Dir();
            case "LEADER" -> new Leaderboard();
            default -> {
                System.out.println("Invalid command : " + command);
                yield null;
            }
        };
    }
}
