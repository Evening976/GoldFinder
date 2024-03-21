package com.example.utils;

import com.example.goldfinder.server.commands.Dir;
import com.example.goldfinder.server.commands.Game_Join;
import com.example.goldfinder.server.commands.Surrounding;

public class CommandParser {
    public static ICommand parse(String command) {
        String prefix = command.split(" ")[0];
        return switch (prefix.toUpperCase()) {
            case "GAME_JOIN" -> new Game_Join();
            case "SURROUNDING" -> new Surrounding();
            case "UP", "DOWN", "LEFT", "RIGHT" -> new Dir();
            default -> {
                System.out.println("Invalid command.");
                yield null;
            }
        };
    }
}
