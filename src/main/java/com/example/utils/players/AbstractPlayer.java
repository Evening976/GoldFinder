package com.example.utils.players;

import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;

public abstract class AbstractPlayer {
    Short gameID = null;
    short id;
    String name;
    final SocketAddress address;
    SelectableChannel client;
    int score;
    int xPos, yPos;

    public AbstractPlayer(SelectableChannel client, String name, SocketAddress address, int xPos, int yPos) {
        this.client = client;
        this.name = name;
        this.address = address;
        score = 0;
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

    public SocketAddress getAddress() {
        return address;
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
        score++;
    }
    public void setName(String playerName) {
        this.name = playerName;
    }
    public int getxPos() {
        return xPos;
    }
    public int getyPos() {
        return yPos;
    }
    @Override
    public String toString(){
        return xPos + "," + yPos;
    }
}
