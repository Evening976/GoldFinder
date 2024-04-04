package com.example.utils.games;

import com.example.goldfinder.server.DispatcherServer;
import com.example.utils.players.AbstractPlayer;

public class GFGame extends AbstractGame {
    int discoveredCount = 0;
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
            discoveredCount++;
        }
    }

    public int getDiscoveredCount() {
        return grid.getColumnCount() * grid.getRowCount();
    }

    public int getDiscoveredCells() {
        return discoveredCount;
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
