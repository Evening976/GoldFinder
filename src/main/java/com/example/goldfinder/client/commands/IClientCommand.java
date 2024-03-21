package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.ClientBoi;

public interface  IClientCommand {
     String run(ClientBoi boi, String params);
     String response(ClientBoi boi, String msg);
}
