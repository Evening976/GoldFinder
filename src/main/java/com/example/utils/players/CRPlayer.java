package com.example.utils.players;

import java.nio.channels.SelectableChannel;

public class CRPlayer extends AbstractPlayer {

    boolean isCop;
    boolean isDead;

    public CRPlayer(SelectableChannel client,String name, int xPos, int yPos){
        super(client, name, xPos, yPos);
    }

    public void setCop(boolean isCop){
        this.isCop = isCop;
    }

    public boolean isCop(){
        return isCop;
    }

    public void setDead(boolean isDead){
        this.isDead = isDead;
    }
    public boolean isDead(){
        return isDead;
    }

    @Override
    public String toString() {
        return "Player " + name + " is a " + (isCop ? "Cop" : "Robber") + " at (" + xPos + ", " + yPos + ")";
    }
}
