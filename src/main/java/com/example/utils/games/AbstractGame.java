package com.example.utils.games;

import com.example.goldfinder.server.AppServer;
import com.example.goldfinder.server.Grid;
import com.example.utils.players.AbstractPlayer;
import com.example.utils.players.GFPlayer;
import javafx.application.Platform;

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
        this.grid = new Grid(AppServer.COLUMN_COUNT, AppServer.ROW_COUNT, new Random());
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
        System.out.println("isFree");
        //boolean p =  !grid.downWall(xpos, ypos) && !grid.upWall(xpos, ypos) && !grid.leftWall(xpos, ypos) && !grid.rightWall(xpos, ypos);
        for (AbstractPlayer player : players) {
            if (player.getxPos() == xpos && player.getyPos() == ypos) return false;
        }
        return true;
    }

    public abstract String getUp(int xpos, int ypos);

    public abstract String getDown(int xpos, int ypos);

    public abstract String getLeft(int xpos, int ypos);

    public abstract String getRight(int xpos, int ypos);

    private void spawnPlayer(AbstractPlayer p) {
        while (true) {
            int xpos = (int) (Math.random() * grid.getColumnCount());
            int ypos = (int) (Math.random() * grid.getRowCount());
            boolean isFree = isFree(xpos, ypos);
            if (isFree) {
                p.move(xpos, ypos);
                break;
            }
        }
    }

    public void movePlayer(AbstractPlayer p, int xpos, int ypos) {
        players.get(players.indexOf(p)).move(xpos, ypos);
    }

    public void removePlayer(AbstractPlayer player) {
        players.remove(player);
    }

    public AbstractPlayer getPlayer(AbstractPlayer p) {
        return players.get(players.indexOf(p));
    }

    public void collectGold(GFPlayer p) {
        if (grid.hasGold(p.getxPos(), p.getyPos())) {
            p.collectGold();
            grid.removeGold(p.getxPos(), p.getyPos());
        }
    }

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
        return players;
    }

    public void setHasEnded(boolean hasEnded) {
        this.hasEnded = hasEnded;
    }

    public void restartGame(){
        for (AbstractPlayer player : getPlayers()){
            System.out.println("Restarting game");
        }
    }

    public boolean hasEnded() {
        return hasEnded;
    }
}
