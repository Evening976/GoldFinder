package com.example.goldfinder.server.commands.gameserver;

public class GameServerCommandParser {
    public static GameServerCommand parseCommand(String command) {
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


    private static GameServerCommand parseButColon(String command){
        String prefix = command.split(":")[0];
        return switch (prefix.toUpperCase()) {
            case "GAME_JOIN" -> new Game_Join();
            case "UP", "DOWN", "LEFT", "RIGHT" -> new Dir();
            case "LEADER" -> new Leaderboard();
            default -> {
                System.out.println("Invalid command : " + command);
                yield null;
            }
        };
    }
}
