package com.example.utils.Games;

import com.example.goldfinder.server.Grid;
import com.example.utils.Players.AbstractPlayer;
import com.example.utils.Players.GFPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class gdGame extends Game{

    public gdGame(int maxPlayers) {
        super(maxPlayers);
    }

    public void addPlayer(GFPlayer player) {
        if (!isRunning) {
            players.add(player);
            spawnPlayer(player);
            System.out.println("Player added to game " + this + " " + players.size() + "/" + maxPlayers);
            if (players.size() == maxPlayers) {
                isRunning = true;
            }
        }
    }

    public String getSurrounding(int xpos, int ypos) {
        return "up:" + getUp(xpos, ypos) + "down:" + getDown(xpos, ypos) + "left:" + getLeft(xpos, ypos) + "right:" + getRight(xpos, ypos);
    }

    private boolean isFree(int xpos, int ypos) {
        boolean p = !grid.hasGold(xpos, ypos) && !grid.downWall(xpos, ypos) && !grid.upWall(xpos, ypos) && !grid.leftWall(xpos, ypos) && !grid.rightWall(xpos, ypos);
        for (AbstractPlayer player : players) {
            if (player.getxPos() == xpos && player.getyPos() == ypos) return false;
        }
        return p;
    }

    public String getUp(int xpos, int ypos) {
        if (grid.upWall(xpos, ypos) || ypos == 0) return "WALL ";
        for (AbstractPlayer p : players) {
            if (p.getxPos() == xpos && p.getyPos() == ypos - 1) return "PLAYER" + players.indexOf(p) + " ";
        }
        if (grid.hasGold(xpos, ypos - 1)) return "GOLD ";
        return "EMPTY ";
    }

    public String getDown(int xpos, int ypos) {
        if (grid.downWall(xpos, ypos) || ypos == grid.getRowCount()) return "WALL ";
        for (AbstractPlayer p : players) {
            if (p.getxPos() == xpos && p.getyPos() == ypos + 1) return "PLAYER" + players.indexOf(p) + " ";
        }
        if (grid.hasGold(xpos, ypos + 1)) return "GOLD ";
        return "EMPTY ";
    }

    public String getLeft(int xpos, int ypos) {
        if (grid.leftWall(xpos, ypos) || xpos == 0) return "WALL ";
        for (AbstractPlayer p : players) {
            if (p.getxPos() == xpos - 1 && p.getyPos() == ypos) return "PLAYER" + players.indexOf(p) + " ";
        }
        if (grid.hasGold(xpos - 1, ypos)) return "GOLD ";
        return "EMPTY ";
    }

    public String getRight(int xpos, int ypos) {
        if (grid.rightWall(xpos, ypos) || xpos == grid.getColumnCount()) return "WALL ";
        for (AbstractPlayer p : players) {
            if (p.getxPos() == xpos + 1 && p.getyPos() == ypos) return "PLAYER" + players.indexOf(p) + " ";
        }
        if (grid.hasGold(xpos + 1, ypos)) return "GOLD ";
        return "EMPTY ";
    }

    private void spawnPlayer(GFPlayer p) {
        while (true) {
            int xpos = (int) (Math.random() * grid.getColumnCount());
            int ypos = (int) (Math.random() * grid.getRowCount());
            if (isFree(xpos, ypos)) {
                p.move(xpos, ypos);
                break;
            }
        }
    }

    public void collectGold(GFPlayer p) {
        if (grid.hasGold(p.getxPos(), p.getyPos())) {
            p.collectGold();
            grid.removeGold(p.getxPos(), p.getyPos());
        }
    }


    public void movePlayer(GFPlayer p, int xpos, int ypos) {
        players.get(players.indexOf(p)).move(xpos, ypos);
        System.out.println("Player moved to " + p);
    }

    public void removePlayer(GFPlayer player) {
        players.remove(player);
    }

    public AbstractPlayer getPlayer(GFPlayer p) {
        return players.get(players.indexOf(p));
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public String toString() {
        return "Game with " + players.size() + " players";
    }
}