package com.example.utils.players;

import java.nio.channels.SelectableChannel;

public class GFPlayer extends AbstractPlayer {
    public GFPlayer(SelectableChannel client,String name, int xPos, int yPos) {
        super(client, name, xPos, yPos);
    }
}
