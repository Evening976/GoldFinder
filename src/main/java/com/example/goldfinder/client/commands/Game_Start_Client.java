package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.ClientBoi;

public class Game_Start_Client implements IClientCommand{
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String run(ClientBoi boi, String params) {
        System.out.println("Game started");
        boi.setPlaying(true);
        return "";
    }

    @Override
    public String response(ClientBoi boi, String msg) {
        return "";
    }
}
