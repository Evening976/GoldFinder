package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.AppClient;
import com.example.goldfinder.client.ClientBoi;

public class Game_End extends IClientCommand{
    @Override
    public String run(ClientBoi boi, String params) {
        AppClient.getController().initialize();
        return null;
    }
}