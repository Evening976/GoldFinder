package com.example.utils.Players;

import javafx.scene.paint.Color;

public enum PlayerColor {
    ORANGE(0),
    RED (1),
    YELLOW (2),
    GREEN (3),
    PINK(4);

    private final int id;
    PlayerColor(int i) {
        this.id = i;
    }

    public Color getColor(){
        if(id == 0) return Color.ORANGE;
        if(id == 1) return Color.RED;
        if(id == 4) return Color.PINK;
        if(id == 3) return Color.GREEN;
        return Color.YELLOW;
    }
}
