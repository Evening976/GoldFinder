package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.ClientBoi;

public class Client_Join implements IClientCommand{
    @Override
    public String run(ClientBoi boi, String params) {
        if(boi.isPlaying()) return "You are already in a game!";
        boi.sendMessage("GAME_JOIN " + params);
        boi.setPlaying(true);
        return "";
    }

    @Override
    public String response(ClientBoi boi, String msg) {
        return msg;
    }
}
