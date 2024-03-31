package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.AppClient;
import com.example.goldfinder.client.ClientBoi;

public class Client_Disconnect implements IClientCommand{
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String run(ClientBoi boi, String params) {
        System.out.println("Disconnected from server");
        AppClient.getController().exitApplication();
        return null;
    }

    @Override
    public String response(ClientBoi boi, String msg) {
        return null;
    }
}
