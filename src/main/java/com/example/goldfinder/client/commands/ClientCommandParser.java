package com.example.goldfinder.client.commands;

public class ClientCommandParser {
    public static IClientCommand parseCommand(String command) {
        if(command.isEmpty()) return null;
        return switch(command.split(" ")[0].toUpperCase()) {
            case "GAME_START" -> new Game_Start_Client();
            case "GAME_END", "GAME_FULL" -> new Client_End();
            case "REDIRECT" -> new Redirect();
            default -> {
                System.out.println("Invalid command received : " + command); yield null;}
        };
    }
}
