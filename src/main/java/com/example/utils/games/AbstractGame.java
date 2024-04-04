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
        this.grid = new Grid(DispatcherServer.COLUMN_COUNT * maxPlayers, DispatcherServer.ROW_COUNT * maxPlayers, new Random());
        System.out.println("Game size : " + grid.getRowCount() + "x" + grid.getColumnCount());
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

    public abstract boolean isValidMove(String move);
    public abstract String updateGame(AbstractPlayer p, String cellState, int nextX, int nextY);
    public abstract void endGame();
    public String getSurrounding(int xpos, int ypos) {
        return "up:" + getUp(xpos, ypos) + "down:" + getDown(xpos, ypos) + "left:" + getLeft(xpos, ypos) + "right:" + getRight(xpos, ypos);
    }

    protected boolean isFree(int xpos, int ypos){
        return getPlayerFromCoordinates(xpos, ypos) == null && !grid.hasGold(xpos, ypos);
    }

    public String getUp(int xpos, int ypos){
        if(grid.upWall(xpos, ypos) || ypos == 0) return "WALL ";
        return getObstacles(xpos, ypos, 0, -1);
    }

    public  String getDown(int xpos, int ypos){
        if(grid.downWall(xpos, ypos) || ypos == grid.getRowCount()) return "WALL ";
        return getObstacles(xpos, ypos, 0, 1);
    }

    public  String getLeft(int xpos, int ypos){
        if(grid.leftWall(xpos, ypos) || xpos == 0) return "WALL ";
        return getObstacles(xpos, ypos, -1, 0);
    }

    public  String getRight(int xpos, int ypos){
        if(grid.rightWall(xpos, ypos) || xpos == grid.getColumnCount()) return "WALL ";
        return getObstacles(xpos, ypos, 1, 0);
    }

    protected abstract String getObstacles(int xpos, int ypos, int xdir, int ydir);
    protected abstract void spawnPlayer(AbstractPlayer p);

    protected abstract boolean canCollectGold(AbstractPlayer p);

    public int getGoldCount() {
        return grid.getGoldCount();
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

    public void collectGold(AbstractPlayer p){
        if(grid.hasGold(p.getxPos(), p.getyPos()) && canCollectGold(p)){
            p.collectGold();
            grid.removeGold(p.getxPos(), p.getyPos());
        }
    }

    public boolean isRunning() {
        return isRunning;
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
