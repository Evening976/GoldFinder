package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.ClientBoi;

public class Move_Command implements IClientCommand{
    @Override
    public String getName() {
        return "MOVE";
    }

    @Override
    public String run(ClientBoi boi, String params) {
        if(!boi.isPlaying()) return "";
        switch(params){
            case "UP" -> boi.sendMessage("UP");
            case "DOWN"-> boi.sendMessage("DOWN");
            case "LEFT"-> boi.sendMessage("LEFT");
            case "RIGHT"-> boi.sendMessage("RIGHT");
        }
        return "";
    }

    @Override
    public String response(ClientBoi boi, String msg) {
        return msg;
    }
}
