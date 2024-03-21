package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.ClientBoi;

public abstract class IClientCommand {
    CommandState state;
    public abstract String run(ClientBoi boi, String params);

    public CommandState getState(){
        return state;
    }
}
