package com.example.goldfinder.server.commands.dispatcherserver;

public class DispatcherServerCommandParser {
    public static DispatcherServerCommand parseCommand(String command) {
        String prefix = command.split(" ")[0];
        return switch (prefix.toUpperCase()) {
            case "GAME_JOIN" -> new D_Game_Join();
            default -> {
                System.out.println("Invalid command : " + command);
                yield null;
            }
        };
    }
}
