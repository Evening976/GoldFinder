package com.example.utils.games;

import com.example.utils.players.AbstractPlayer;
import com.example.utils.players.CRPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CRGame extends AbstractGame{
    List<AbstractPlayer> cops;
    Map<AbstractPlayer, String> robbers;
    int goldCount;

    public CRGame(int maxPlayers) {
        super(maxPlayers);
        this.cops = new ArrayList<>();
        this.robbers = new HashMap<>();
        this.goldCount = countGold();
    }

    protected void spawnPlayer(AbstractPlayer p) {
        while (true) {
            int xpos = (int) (Math.random() * grid.getColumnCount());
            int ypos = (int) (Math.random() * grid.getRowCount());
            if (cops.isEmpty() || cops.size() < maxPlayers / 2) {
                System.out.println(cops.size() + " cops");
                setCop((CRPlayer) p);
            } else {
                setRobber((CRPlayer) p);
            }
            if (isFree(xpos, ypos)) {
                p.move(xpos, ypos);
                break;
            }
        }
    }

    private boolean isPlayerACop(int xpos, int ypos) {
        CRPlayer p = (CRPlayer) getPlayerFromCoordinates(xpos, ypos);
        return p != null && p.isCop();
    }

    @Override
    public String getUp(int xpos, int ypos) {
        if (grid.upWall(xpos, ypos) || ypos == 0) return "WALL ";
        return getNeighbour(xpos, ypos, 0, -1);
    }

    @Override
    public String getDown(int xpos, int ypos) {
        if (grid.downWall(xpos, ypos) || ypos == grid.getRowCount()) return "WALL ";
        return getNeighbour(xpos, ypos, 0, 1);
    }

    @Override
    public String getLeft(int xpos, int ypos) {
        if (grid.leftWall(xpos, ypos) || xpos == 0) return "WALL ";
        return getNeighbour(xpos, ypos, -1, 0);
    }

    @Override
    public String getRight(int xpos, int ypos) {
        if (grid.rightWall(xpos, ypos) || xpos == grid.getColumnCount()) return "WALL ";
        return getNeighbour(xpos, ypos, 1, 0);
    }

    public void setCop(CRPlayer p) {
        p.setCop(true);
        cops.add(p);
    }

    public void setRobber(CRPlayer p) {
        p.setCop(false);
        robbers.put(p, "FREE");
    }

    public CRPlayer getPlayer(AbstractPlayer p) {
        return (CRPlayer) players.get(players.indexOf(p));
    }
    @Override
    public String toString() {
        return "Cops and Robbers game with " + players.size() + " players";
    }

    public void collectGold(AbstractPlayer p) {
        if (grid.hasGold(p.getxPos(), p.getyPos()) && !((CRPlayer) p).isCop()) {
            p.collectGold();
            goldCount--;
            grid.removeGold(p.getxPos(), p.getyPos());
        }
    }

    public Map<AbstractPlayer,String> getRobbers(){
        return robbers;
    }

    public void catchRobber(AbstractPlayer cop, AbstractPlayer robber) {
        cop.collectGold();
        robbers.put(robber,"CAUGHT");
        removePlayer(robber);
        System.out.println("catching robber");
    }

    private int countGold(){
        int count = 0;
        for (int i = 0; i < grid.getColumnCount(); i++) {
            for (int j = 0; j < grid.getRowCount(); j++) {
                if (grid.hasGold(i, j)) {
                    count++;
                }
            }
        }
        return count;
    }

    private String getNeighbour(int xpos, int ypos, int x, int y){
        CRPlayer p = (CRPlayer) getPlayerFromCoordinates(xpos + x, ypos + y);
        if(p != null){
            if (p.isCop()) {
                return isPlayerACop(xpos, ypos) ? "ALLY " : "ENEMY ";
            } else {
                return isPlayerACop(xpos, ypos) ? "ENEMY " : "ALLY ";
            }
        }
        if(grid.hasGold(xpos + x, ypos + y)){
            return "GOLD ";
        }
        return "EMPTY ";
    }

    public int getGoldCount(){
        return goldCount;
    }
}
