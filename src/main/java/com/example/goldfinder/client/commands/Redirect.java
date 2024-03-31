package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.ClientBoi;

public class Redirect implements IClientCommand{
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String run(ClientBoi boi, String params) {
        System.out.println("Redirecting to " + params.split(":")[1] + " " + params.split(":")[2]);
        return params.split(" ")[1].split(":")[1] + " " + params.split(":")[2];
    }

    @Override
    public String response(ClientBoi boi, String msg) {
        return null;
    }
}
