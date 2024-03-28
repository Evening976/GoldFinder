package com.example.utils;

import java.io.Serializable;
import java.nio.channels.SelectableChannel;

public class Player extends AbstractPlayer{
    public Player(SelectableChannel client, String name, ConnectionMode connectionMode, int xPos, int yPos) {
        super(client, name, connectionMode, xPos, yPos);
    }
}
