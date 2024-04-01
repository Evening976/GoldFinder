package com.example.utils.games;

import com.example.goldfinder.server.DispatcherServer;
import com.example.goldfinder.server.Grid;
import com.example.utils.players.AbstractPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractGame {
    boolean isRunning = false;
    boolean hasEnded = false;
    int maxPlayers;
    List<AbstractPlayer> players;
    Grid grid;

    public AbstractGame(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>();
        this.grid = new Grid(DispatcherServer.COLUMN_COUNT, DispatcherServer.ROW_COUNT, new Random());

    }

    public void addPlayer(AbstractPlayer player) {
        if (!isRunning) {
            players.add(player);
            spawnPlayer(player);
            System.out.println("spawned");
            System.out.println("Player added to game " + this + " " + players.size() + "/" + maxPlayers);
            if (players.size() == maxPlayers) {
                isRunning = true;
            }
        }
    }

    public String getSurrounding(int xpos, int ypos) {
        return "up:" + getUp(xpos, ypos) + "down:" + getDown(xpos, ypos) + "left:" + getLeft(xpos, ypos) + "right:" + getRight(xpos, ypos);
    }

    protected boolean isFree(int xpos, int ypos){
        return getPlayerFromCoordinates(xpos, ypos) == null;
    }

    public abstract String getUp(int xpos, int ypos);

    public abstract String getDown(int xpos, int ypos);

    public abstract String getLeft(int xpos, int ypos);

    public abstract String getRight(int xpos, int ypos);

    protected abstract void spawnPlayer(AbstractPlayer p);

    public void movePlayer(AbstractPlayer p, int xpos, int ypos) {
        players.get(players.indexOf(p)).move(xpos, ypos);

    }

    public void removePlayer(AbstractPlayer player) {
        players.remove(player);
    }

    public AbstractPlayer getPlayer(AbstractPlayer p) {
        return players.get(players.indexOf(p));
    }

    public abstract void collectGold(AbstractPlayer p);

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isSolo() {
        return maxPlayers == 1;
    }

    @Override
    public String toString() {
        return "Game with " + players.size() + " players";

    }
    public List<AbstractPlayer> getPlayers() {
        System.out.println(players.size() + " players are in game" );
        return players;
    }

    public void setHasEnded(boolean hasEnded) {
        this.hasEnded = hasEnded;
    }

    public AbstractPlayer getPlayerFromCoordinates(int xpos, int ypos){
        for (AbstractPlayer p : players) {
            if (p.getxPos() == xpos && p.getyPos() == ypos) {
                return p;
            }
        }
        return null;
    }
    public boolean hasEnded() {
        return hasEnded;
    }
}
