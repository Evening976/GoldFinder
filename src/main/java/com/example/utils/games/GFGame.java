package com.example.utils.games;

import com.example.utils.players.AbstractPlayer;

public class GFGame extends AbstractGame {
    boolean isSolo = false;
    public GFGame(){
        super(1);
    }

    public GFGame(int maxPlayers) {
        super(maxPlayers);
    }

    public String getSurrounding(int xpos, int ypos) {
        return "up:" + getUp(xpos, ypos) + "down:" + getDown(xpos, ypos) + "left:" + getLeft(xpos, ypos) + "right:" + getRight(xpos, ypos);
    }
    @Override
    public String getUp(int xpos, int ypos) {
        if (grid.upWall(xpos, ypos) || ypos == 0) return "WALL ";
        for (AbstractPlayer p : players) {
            if (p.getxPos() == xpos && p.getyPos() == ypos - 1) return "PLAYER" + players.indexOf(p) + " ";
        }
        if (grid.hasGold(xpos, ypos - 1)) return "GOLD ";
        return "EMPTY ";
    }

    @Override
    public String getDown(int xpos, int ypos) {
        if (grid.downWall(xpos, ypos) || ypos == grid.getRowCount()) return "WALL ";
        for (AbstractPlayer p : players) {
            if (p.getxPos() == xpos && p.getyPos() == ypos + 1) return "PLAYER" + players.indexOf(p) + " ";
        }
        if (grid.hasGold(xpos, ypos + 1)) return "GOLD ";
        return "EMPTY ";
    }

    @Override
    public String getLeft(int xpos, int ypos) {
        if (grid.leftWall(xpos, ypos) || xpos == 0) return "WALL ";
        for (AbstractPlayer p : players) {
            if (p.getxPos() == xpos - 1 && p.getyPos() == ypos) return "PLAYER" + players.indexOf(p) + " ";
        }
        if (grid.hasGold(xpos - 1, ypos)) return "GOLD ";
        return "EMPTY ";
    }

    @Override
    public String getRight(int xpos, int ypos) {
        if (grid.rightWall(xpos, ypos) || xpos == grid.getColumnCount()) return "WALL ";
        for (AbstractPlayer p : players) {
            if (p.getxPos() == xpos + 1 && p.getyPos() == ypos) return "PLAYER" + players.indexOf(p) + " ";
        }
        if (grid.hasGold(xpos + 1, ypos)) return "GOLD ";
        return "EMPTY ";
    }
//    public GFPlayer getPlayer(GFPlayer p) {
//        return (GFPlayer) players.get(players.indexOf(p));
//    }
    public boolean isSolo() {
        return isSolo;
    }
    @Override
    public String toString() {
        return "Goldfinder game with " + players.size() + " players";
    }
}
