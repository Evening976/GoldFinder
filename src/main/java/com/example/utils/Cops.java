package com.example.utils;

import java.nio.channels.SelectableChannel;

public class Cops extends AbstractPlayer{

    public Cops(SelectableChannel client, String name, ConnectionMode connectionMode, int xPos, int yPos){
        super(client, name, connectionMode, xPos, yPos);
    }

}
