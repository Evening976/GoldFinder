package com.example.utils.Players;

import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;

public class GFPlayer extends AbstractPlayer {
    public GFPlayer(SelectableChannel client, String name, SocketAddress address, int xPos, int yPos) {
        super(client, name, address, xPos, yPos);
    }
}
