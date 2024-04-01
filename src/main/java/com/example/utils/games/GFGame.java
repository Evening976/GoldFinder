package com.example.utils.games;

import com.example.utils.players.AbstractPlayer;

public class GFGame extends AbstractGame {
    int maxCells = 0;
    boolean[][] discoveredCells;
    boolean isSolo = false;

    public GFGame() {
        super(1);
    }

    public GFGame(int maxPlayers) {
        super(maxPlayers);
    }
    @Override
    protected void spawnPlayer(AbstractPlayer p) {
        while (true) {
            int xpos = (int) (Math.random() * grid.getColumnCount());
            int ypos = (int) (Math.random() * grid.getRowCount());
            if (isFree(xpos, ypos)) {
                p.move(xpos, ypos);
                setDiscoveredCell(xpos, ypos);
                break;
            }
        }
    }

    @Override
    protected boolean canCollectGold(AbstractPlayer p) {
        return true;
    }

    public boolean isSolo() {
        return isSolo;
    }

    public void setDiscoveredCell(int xpos, int ypos) {
        if (discoveredCells == null) {
            discoveredCells = new boolean[grid.getColumnCount()][grid.getRowCount()];
        }
        if (!discoveredCells[xpos][ypos]) {
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

    protected String getObstacles(int xpos, int ypos, int x, int y) {
        AbstractPlayer p = getPlayerFromCoordinates(xpos + x, ypos + y);
        if (p != null) {
            return "PLAYER" + players.indexOf(p) + " ";
        }
        if (grid.hasGold(xpos + x, ypos + y)) return "GOLD ";
        return "EMPTY ";
    }
}
