package com.example.utils;

import com.example.goldfinder.client.commands.*;

public class ClientCommandParser {
    public static IClientCommand parseCommand(String command) {
        return switch(command.split(" ")[0].toUpperCase()) {
            case "GAME_START" -> new Game_Start();
            case "GAME_END" -> new Game_End();
            case "VALID_MOVE" -> new Valid_Move();
            case "SCORE" -> new Score();
            case "INVALID_MOVE" -> null;
            default -> {
                System.out.println("Invalid command.");
                yield null;
            }
        };
    }
}
