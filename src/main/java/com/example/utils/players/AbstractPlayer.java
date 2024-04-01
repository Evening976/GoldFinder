package com.example.utils.players;

import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;

public abstract class AbstractPlayer {
    Short gameID = null;
    short id;
    String name;
    SocketAddress address;
    SelectableChannel client;
    int score;
    int xPos, yPos;

    public AbstractPlayer(SelectableChannel client, String name, int xPos, int yPos) {
        this.name = name;
        this.client = client;
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

    public void setAddress(SocketAddress address) {
        this.address = address;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
