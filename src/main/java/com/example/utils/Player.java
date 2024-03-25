package com.example.utils;

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

    public void move(int x, int y) {
        xPos += x;
        yPos += y;
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

    public void collectGold() {
        goldCollected++;
    }

    public void setName(String playerName) {
        this.name = playerName;
    }

    public int getxPos() {
        return xPos;
    }

    @Override
    public String toString(){
        return xPos + "," + yPos;
    }

    public int getyPos() {
        return yPos;
    }
}
