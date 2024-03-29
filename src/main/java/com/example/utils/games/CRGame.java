package com.example.utils.games;

import com.example.utils.players.AbstractPlayer;
import com.example.utils.players.CRPlayer;
import com.example.utils.players.GFPlayer;

public class CRGame extends AbstractGame{
    public CRGame(int maxPlayers) {
        super(maxPlayers);
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

    public void catchRobber() {
        System.out.println("Robber caught!");
    }

    public CRPlayer getPlayer(GFPlayer p) {
        return (CRPlayer) players.get(players.indexOf(p));
    }
    @Override
    public String toString() {
        return "Cops and Robbers game with " + players.size() + " players";
    }
}
