package com.example.goldfinder;

import com.example.utils.ConnectionMode;

public class Player {
    ConnectionMode connectionMode;
    int goldCollected;
    int xPos, yPos;

    public ConnectionMode getConnectionMode() {
        return connectionMode;
    }
}
