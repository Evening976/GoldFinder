package com.example.utils.Players;

import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;

public class CopsPlayer extends AbstractPlayer {

    boolean isCop;

    public CopsPlayer(SelectableChannel client, String name, SocketAddress address, int xPos, int yPos){
        super(client, name, address, xPos, yPos);
    }

}
