package com.example.utils.games;

import com.example.goldfinder.server.DispatcherServer;
import com.example.utils.players.AbstractPlayer;

public class GFGame extends AbstractGame {
    int maxCells = 0;
    boolean[][] discoveredCells;

    public GFGame() {
        super(DispatcherServer.DEFAULT_PLAYER_COUNT);
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

    public void setDiscoveredCell(int xpos, int ypos) {
        if (discoveredCells == null) {
            discoveredCells = new boolean[grid.getColumnCount()][grid.getRowCount()];
        }
        if (!discoveredCells[xpos][ypos]) {
            discoveredCells[xpos][ypos] = true;
            maxCells++;
        }
        if (xpos + 1 < grid.getColumnCount() && !discoveredCells[xpos+1][ypos] && !grid.rightWall(xpos+1, ypos)) {
            discoveredCells[xpos+1][ypos] = true;
            maxCells++;
        }
        if (xpos-1 >= 0 && !discoveredCells[xpos-1][ypos] && !grid.leftWall(xpos, ypos)) {
            discoveredCells[xpos-1][ypos] = true;
            maxCells++;
        }
        if (ypos+1 < grid.getRowCount() && !discoveredCells[xpos][ypos+1] && !grid.downWall(xpos, ypos)) {
            discoveredCells[xpos][ypos+1] = true;
            maxCells++;
        }
        if(ypos-1 >= 0 && !discoveredCells[xpos][ypos-1] && !grid.upWall(xpos, ypos)) {
            discoveredCells[xpos][ypos-1] = true;
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
