package com.example.utils;

import java.io.Serializable;
import java.nio.channels.SelectableChannel;

public class Player implements Serializable {
    Short gameID = null;
    short id;
    String name;
    ConnectionMode connectionMode;
    SelectableChannel client;
    int goldCollected;
    int xPos, yPos;

    public Player(SelectableChannel client, String name, ConnectionMode connectionMode, int xPos, int yPos) {
        this.client = client;
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

    public void attachToGame(Short gameID, short id) {
        this.gameID = gameID;
        this.id = id;
    }

    public Short getGameID() {
        return gameID;
    }
    public short getPlayerID() {
        return id;
    }

    public SelectableChannel getClient() {
        return client;
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
