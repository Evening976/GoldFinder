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
    public CRGame(int maxPlayers) {
        super(4);
        this.cops = new ArrayList<>();
        this.robbers = new HashMap<>();
    }

    protected void spawnPlayer(AbstractPlayer p) {
        while (true) {
            int xpos = (int) (Math.random() * grid.getColumnCount());
            int ypos = (int) (Math.random() * grid.getRowCount());
            if (cops.size() < maxPlayers / 2 || cops.isEmpty()) {
                setCop((CRPlayer) p);
            } else {
                setRobber( (CRPlayer) p);
            }
            boolean isFree = isFree(xpos, ypos);
            if (isFree) {
                p.move(xpos, ypos);
                break;
            }
        }
    }

    private boolean isPlayerACop(int xpos, int ypos) {
        for (AbstractPlayer p : players) {
            if (p.getxPos() == xpos && p.getyPos() == ypos && ((CRPlayer) p).isCop()) {
                return true;
            }
        }
        System.out.println("false");
        return false;
    }

    @Override
    public String getUp(int xpos, int ypos) {
        if (grid.upWall(xpos, ypos) || ypos == 0) return "WALL ";
        for (AbstractPlayer p : players) {
            if (p.getxPos() == xpos && p.getyPos() == ypos - 1) {
                if (((CRPlayer) p).isCop()) {
                    if (isPlayerACop(xpos, ypos)) {
                        return "ALLY ";
                    }
                    return "ENEMY ";
                } else {
                    if (isPlayerACop(xpos, ypos)) {
                        return "ENEMY ";
                    }
                    return "ALLY ";
                }
            }
        }
        if (grid.hasGold(xpos, ypos - 1)) return "GOLD ";
        return "EMPTY ";
    }

    @Override
    public String getDown(int xpos, int ypos) {
        if (grid.downWall(xpos, ypos) || ypos == grid.getRowCount()) return "WALL ";
        for (AbstractPlayer p : players) {
            if (p.getxPos() == xpos && p.getyPos() == ypos + 1) {
                if (((CRPlayer) p).isCop()) {
                    if (isPlayerACop(xpos, ypos)) {
                        return "ALLY ";
                    } else {
                        return "ENEMY ";
                    }
                } else {
                    if (isPlayerACop(xpos, ypos)) {
                        return "ENEMY ";
                    } else {
                        return "ALLY ";
                    }
                }
            }
        }
        if (grid.hasGold(xpos, ypos + 1)) return "GOLD ";
        return "EMPTY ";
    }

    @Override
    public String getLeft(int xpos, int ypos) {
        if (grid.leftWall(xpos, ypos) || xpos == 0) return "WALL ";
        for (AbstractPlayer p : players) {
            if (p.getxPos() == xpos - 1 && p.getyPos() == ypos) {
                if (((CRPlayer) p).isCop()) {
                    if (isPlayerACop(xpos, ypos)) {
                        return "ALLY ";
                    } else {
                        System.out.println("je ne suis pas un policier");
                        return "ENEMY ";
                    }
                } else {
                    if (isPlayerACop(xpos, ypos)) {
                        return "ENEMY ";
                    } else {
                        return "ALLY ";
                    }
                }
            }
        }
        if (grid.hasGold(xpos - 1, ypos)) return "GOLD ";
        return "EMPTY ";
    }

    @Override
    public String getRight(int xpos, int ypos) {
        if (grid.rightWall(xpos, ypos) || xpos == grid.getColumnCount()) return "WALL ";
        for (AbstractPlayer p : players) {
            if (p.getxPos() == xpos + 1 && p.getyPos() == ypos) {
                if (((CRPlayer) p).isCop()) {
                    if (isPlayerACop(xpos, ypos)) {
                        return "ALLY ";
                    } else {
                        System.out.println("je ne suis pas un policier");
                        return "ENEMY ";
                    }
                } else {
                    if (isPlayerACop(xpos, ypos)) {
                        return "ENEMY ";
                    } else {
                        return "ALLY ";
                    }
                }
            }
        }
        if (grid.hasGold(xpos + 1, ypos)) return "GOLD ";
        return "EMPTY ";
    }

    public void catchRobber() {
        System.out.println("Robber caught!");

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
            grid.removeGold(p.getxPos(), p.getyPos());
        }
    }

    public void catchRobber(AbstractPlayer p) {
        if (grid.hasGold(p.getxPos(), p.getyPos()) && ((CRPlayer) p).isCop()) {
            p.collectGold();
            grid.removeGold(p.getxPos(), p.getyPos());
        }
    }
}
