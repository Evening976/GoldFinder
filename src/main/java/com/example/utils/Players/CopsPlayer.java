package com.example.utils.Players;

import com.example.utils.ConnectionMode;

import java.nio.channels.SelectableChannel;

public class CopsPlayer extends AbstractPlayer {

    boolean isCop;

    public CopsPlayer(SelectableChannel client, String name, ConnectionMode connectionMode, int xPos, int yPos){
        super(client, name, connectionMode, xPos, yPos);
    }

}
