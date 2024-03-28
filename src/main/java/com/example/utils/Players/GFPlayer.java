package com.example.utils.Players;

import com.example.utils.ConnectionMode;

import java.nio.channels.SelectableChannel;

public class GFPlayer extends AbstractPlayer {
    public GFPlayer(SelectableChannel client, String name, ConnectionMode connectionMode, int xPos, int yPos) {
        super(client, name, connectionMode, xPos, yPos);
    }
}
