package com.example.goldfinder;

import com.example.utils.ConnectionMode;

public class Player {

    String name;
    ConnectionMode connectionMode;
    int goldCollected;
    int xPos, yPos;

    public Player(String name, ConnectionMode connectionMode, int xPos, int yPos) {
        this.name = name;
        this.connectionMode = connectionMode;
        goldCollected = 0;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public String getName() {
        return name;
    }

    public ConnectionMode getConnectionMode() {
        return connectionMode;
    }
}
