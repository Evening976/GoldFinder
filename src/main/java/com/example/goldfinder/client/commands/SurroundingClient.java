package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.ClientBoi;

public class SurroundingClient implements IClientCommand{
    @Override
    public String getName() {
        return "SURROUNDING";
    }

    @Override
    public String run(ClientBoi boi, String params) {
        if(boi.isPlaying()) {
            boi.sendMessage("SURROUNDING");
            return "";
        }
        return "Faut jouer enfaite";
    }

    @Override
    public String response(ClientBoi boi, String msg) {
        return msg;
    }
}
