package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.ClientBoi;

public class Client_Join implements IClientCommand{
    @Override
    public String getName() {
        return "GAME_JOIN";
    }

    @Override
    public String run(ClientBoi boi, String params) {
        if(boi.isPlaying()) return "You are already in a game!";
        boi.sendMessage("GAME_JOIN " + params);
        return "";
    }

    @Override
    public String response(ClientBoi boi, String msg) {
        if(msg.contains("GAME_START")){
            boi.setPlaying(true);
            return "Game started!";
        }
        return msg;
    }
}
