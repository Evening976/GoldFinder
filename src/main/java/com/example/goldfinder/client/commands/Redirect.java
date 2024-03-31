package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.ClientBoi;

import java.net.InetSocketAddress;

public class Redirect implements IClientCommand{
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String run(ClientBoi boi, String params) {
        boi.redirect(new InetSocketAddress(params.split(" ")[1].split(":")[0], Integer.parseInt(params.split(" ")[1].split(":")[1])));
        return null;
    }

    @Override
    public String response(ClientBoi boi, String msg) {
        return null;
    }
}
