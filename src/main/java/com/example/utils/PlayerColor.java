package com.example.utils;

import javafx.scene.paint.Color;

public enum PlayerColor {
    BLUE(0),
    RED (1),
    YELLOW (2),
    GREEN (3);

    private final int id;
    PlayerColor(int i) {
        this.id = i;
    }

    public Color getColor(){
        if(id == 0) return Color.BLUE;
        if(id == 1) return Color.RED;
        if(id == 2) return Color.YELLOW;
        return Color.GREEN;
    }
}
