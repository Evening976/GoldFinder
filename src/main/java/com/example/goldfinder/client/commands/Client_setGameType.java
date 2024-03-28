package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.ClientBoi;

public class Client_setGameType implements IClientCommand{

    @Override
    public String getName() {
        return "SET_GAME_TYPE";
    }

    @Override
    public String run(ClientBoi boi, String params) {
        if (boi.isPlaying()) return "You are already in a game!";
        boi.sendMessage("SET_GAME_TYPE " + params);
        return "";
    }

    @Override
    public String response(ClientBoi boi, String msg) {
        if (msg.contains("GAME_MODE_SET")) {
            return "Game mode set!";
        }
        return msg;
    }
}
