package com.example.utils;

import com.example.goldfinder.server.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class gdGame {
    boolean isReady = false;
    int maxPlayers;
    List<Player> players;
    Grid grid;

    public gdGame(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>();
        this.grid = new Grid(20, 20, new Random());
    }

    public void addPlayer(Player player) {
        if(players.size() < maxPlayers) players.add(player);
        else isReady = true;
    }

    public String getSurrounding(int xpos, int ypos){
        return getUp(xpos, ypos) + getDown(xpos, ypos) + getLeft(xpos, ypos) + getRight(xpos, ypos);
    }

    public String getUp(int xpos, int ypos){
        if(grid.upWall(xpos, ypos)) return "up: WALL ";
        for(Player p : players){
            if(p.getxPos() == xpos && p.getyPos() == ypos - 1) return "up: PLAYER" + players.indexOf(p) + " ";
        }
        if(grid.hasGold(xpos, ypos - 1)) return "up: GOLD ";
        return "up: EMPTY ";
    }

    public String getDown(int xpos, int ypos){
        if(grid.downWall(xpos, ypos)) return "down: WALL ";
        for(Player p : players){
            if(p.getxPos() == xpos && p.getyPos() == ypos + 1) return "down: PLAYER" + players.indexOf(p) + " ";
        }
        if(grid.hasGold(xpos, ypos + 1)) return "down: GOLD ";
        return "down: EMPTY ";
    }

    public String getLeft(int xpos, int ypos){
        if(grid.leftWall(xpos, ypos)) return "left: WALL ";
        for(Player p : players){
            if(p.getxPos() == xpos - 1 && p.getyPos() == ypos) return "left: PLAYER" + players.indexOf(p)+ " ";
        }
        if(grid.hasGold(xpos - 1, ypos)) return "left: GOLD ";
        return "left: EMPTY ";
    }

    public String getRight(int xpos, int ypos){
        if(grid.rightWall(xpos, ypos)) return "right: WALL ";
        for(Player p : players){
            if(p.getxPos() == xpos + 1 && p.getyPos() == ypos) return "right: PLAYER" + players.indexOf(p)+ " ";
        }
        if(grid.hasGold(xpos + 1, ypos)) return "right: GOLD ";
        return "right: EMPTY ";
    }

    public void removePlayer(Player player) {
        if(players.remove(player)) isReady = false;
    }

    public boolean isReady() {
        return isReady;
    }
}
