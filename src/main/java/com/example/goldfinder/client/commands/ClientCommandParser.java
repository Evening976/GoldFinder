package com.example.goldfinder.client.commands;

public class ClientCommandParser {
    public static IClientCommand parseCommand(String command) {
        if(command.isEmpty()) return null;
        return switch(command.split(" ")[0].toUpperCase()) {
            case "GAME_START" -> new Game_Start_Client();
            case "GAME_END" -> new Game_End();
            case "VALID_MOVE" -> new Valid_Move();
            case "SCORE" -> new Score();
            case "REDIRECT" -> new Redirect();
            case "DISCONNECT" -> new Client_Disconnect();
            default -> {
                System.out.println("Invalid command received : " + command); yield null;}
        };
    }
}
