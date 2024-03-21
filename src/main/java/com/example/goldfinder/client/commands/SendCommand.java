package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.ClientBoi;

public class SendCommand {
    public static String run(IClientCommand command, ClientBoi boi, String params) {
        return command.run(boi, params);
    }
}
