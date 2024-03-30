package com.example.utils.players;

import javafx.scene.paint.Color;

import java.nio.channels.SelectableChannel;

public class CRPlayer extends AbstractPlayer {

    boolean isCop;

    public CRPlayer(SelectableChannel client,String name, int xPos, int yPos){
        super(client, name, xPos, yPos);
    }

    public void setCop(boolean isCop){
        this.isCop = isCop;
    }

    public boolean isCop(){
        return isCop;
    }



}
