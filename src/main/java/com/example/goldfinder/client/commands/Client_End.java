package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.AppClient;
import com.example.goldfinder.client.ClientBoi;

public class Client_End implements IClientCommand{
    @Override
    public String getName() {
        return "GAME_END";
    }

    @Override
    public String run(ClientBoi boi, String params) {
        if(AppClient.getController() != null) AppClient.getController().restartButtonAction();
        return null;
    }

    @Override
    public String response(ClientBoi boi, String msg) {
        return null;
    }
}
