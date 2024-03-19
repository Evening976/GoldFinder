package com.example.goldfinder;

import com.example.utils.ConnectionMode;

import java.io.Serializable;

public class Player implements Serializable {
    Short gameID = null;
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

    public void attachToGame(Short gameID) {
        this.gameID = gameID;
    }

    public Short getGameID() {
        return gameID;
    }

    public void setName(String playerName) {
        this.name = playerName;
    }
}
