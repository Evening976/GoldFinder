package com.example.utils;

import com.example.goldfinder.Player;
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
        return "up: " + grid.upWall(xpos, ypos) + " down: " + grid.downWall(xpos, ypos) + " left: " + grid.leftWall(xpos, ypos) + " right: " + grid.rightWall(xpos, ypos);
    }

    public String getUp(int xpos, int ypos){
        return "up: " + grid.upWall(xpos, ypos);
    }

    public String getDown(int xpos, int ypos){
        return "down: " + grid.downWall(xpos, ypos);
    }

    public String getLeft(int xpos, int ypos){
        return "left: " + grid.leftWall(xpos, ypos);
    }

    public String getRight(int xpos, int ypos){
        return "right: " + grid.rightWall(xpos, ypos);
    }

    public void removePlayer(Player player) {
        if(players.remove(player)) isReady = false;
    }

    public boolean isReady() {
        return isReady;
    }
}
