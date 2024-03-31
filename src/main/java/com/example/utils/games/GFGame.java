package com.example.utils.games;

import com.example.utils.players.AbstractPlayer;
import com.example.utils.players.GFPlayer;

public class GFGame extends AbstractGame {
    int maxCells = 0;
    boolean[][] discoveredCells;
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

    @Override
    protected void spawnPlayer(AbstractPlayer p) {
        while (true) {
            int xpos = (int) (Math.random() * grid.getColumnCount());
            int ypos = (int) (Math.random() * grid.getRowCount());
            boolean isFree = isFree(xpos, ypos);
            if (isFree) {
                p.move(xpos, ypos);
                setDiscoveredCell(xpos, ypos);
                break;
            }
        }
    }

    public void collectGold(AbstractPlayer p) {
        if (grid.hasGold(p.getxPos(), p.getyPos())) {
            p.collectGold();
            grid.removeGold(p.getxPos(), p.getyPos());
        }
    }
    public boolean isSolo() {
        return isSolo;
    }

    public void setDiscoveredCell(int xpos, int ypos){
        if(discoveredCells == null) {
            discoveredCells = new boolean[grid.getColumnCount()][grid.getRowCount()];
        }
        if(!discoveredCells[xpos][ypos]) {
            discoveredCells[xpos][ypos] = true;
            maxCells++;
        }
    }


    public int getMaxCells() {
        return grid.getColumnCount() * grid.getRowCount();
    }

    public int getDiscoveredCells() {
        return maxCells;
    }


    @Override
    public String toString() {
        return "Goldfinder game with " + players.size() + " players";
    }
}
