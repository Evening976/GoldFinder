package com.example.utils.games;

import com.example.utils.players.AbstractPlayer;
import com.example.utils.players.CRPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CRGame extends AbstractGame {
    List<AbstractPlayer> cops;
    Map<AbstractPlayer, String> robbers;
    int goldCount;

    int robberCount;

    public CRGame(int maxPlayers) {
        super(maxPlayers);
        this.cops = new ArrayList<>();
        this.robbers = new HashMap<>();
        this.goldCount = getGoldCount();
        this.robberCount = 0;
    }

    @Override
    public boolean isValidMove(String move) {
        return move.contains("EMPTY") || move.contains("GOLD") || move.contains("ENEMY");
    }

    @Override
    public String updateGame(AbstractPlayer p, String cellState, int nextX, int nextY) {
        System.out.println("updateGame " + p + " " + cellState + " " + nextX + " " + nextY);
        if (((CRPlayer) p).isDead()) {
            return "INVALID_MOVE";
        }

        if (cellState.contains("ENEMY")) {
            catchRobber((CRPlayer) p, (CRPlayer) getPlayerFromCoordinates(p.getxPos() + nextX, p.getyPos() + nextY));
        } else {
            movePlayer(p, nextX, nextY);
        }

        if (cellState.contains("GOLD")) {
            if (((CRPlayer) p).isCop()) {
                return "EMPTY";
            }
            collectGold(p);
            if (getGoldCount() == 0) {
                return "GAME_END";
            }
        }

        for (AbstractPlayer robber : getRobbers().keySet()) {
            if (getRobbers().get(robber).equals("CAUGHT") && !((CRPlayer) robber).isDead()) {
                decreaseRobberCount();
                ((CRPlayer) robber).setDead(true);
            }
        }

        if (getRobberCount() == 0) {
            return "GAME_END";
        }
        return cellState;
    }

    @Override
    public void endGame() {
        setHasEnded(true);
        for (AbstractPlayer p : getPlayers()) {
            setNeutral((CRPlayer) p);
        }
    }

    protected void spawnPlayer(AbstractPlayer p) {
        while (true) {
            int xpos = (int) (Math.random() * grid.getColumnCount());
            int ypos = (int) (Math.random() * grid.getRowCount());
            if (isFree(xpos, ypos)) {
                p.move(xpos, ypos);
                break;
            }
        }
        if (cops.size() < maxPlayers / 2) {
            System.out.println("setting cop ");
            setCop((CRPlayer) p);
        } else {
            System.out.println("setting robber ");
            setRobber((CRPlayer) p);
        }
    }

    @Override
    protected boolean canCollectGold(AbstractPlayer p) {
        return !((CRPlayer) p).isCop();
    }

    private boolean isPlayerACop(int xpos, int ypos) {
        CRPlayer p = (CRPlayer) getPlayerFromCoordinates(xpos, ypos);
        return p != null && p.isCop();
    }

    public void setCop(CRPlayer p) {
        robbers.remove(p);
        p.setCop(true);
        cops.add(p);
    }

    public void setRobber(CRPlayer p) {
        p.setCop(false);
        robbers.put(p, "FREE");
        robberCount++;
    }

    public void setNeutral(CRPlayer p) {
        p.setCop(false);
        p.setDead(false);
        robbers.remove(p);
    }

    public CRPlayer getPlayer(AbstractPlayer p) {
        return (CRPlayer) players.get(players.indexOf(p));
    }

    @Override
    public String toString() {
        return "Cops and Robbers game with " + players.size() + " players";
    }


    public Map<AbstractPlayer, String> getRobbers() {
        return robbers;
    }

    public void catchRobber(CRPlayer p1, CRPlayer p2) {
        if (p1.isCop() && !p2.isCop() && !robbers.get(p2).equals("CAUGHT")) {
            p1.collectGold();
            robbers.remove(p2);
            robbers.put(p2, "CAUGHT");
            System.out.println("CATCH  p1: " + p1 + " p2: " + p2);
        } else if (p2.isCop() && !p1.isCop() && !robbers.get(p1).equals("CAUGHT")) {
            p2.collectGold();
            robbers.remove(p1);
            robbers.put(p1, "CAUGHT");
            System.out.println("CATCH  p1: " + p1 + " p2: " + p2);
        }
    }

    protected String getObstacles(int xpos, int ypos, int x, int y) {
        CRPlayer p = (CRPlayer) getPlayerFromCoordinates(xpos + x, ypos + y);
        if (p != null) {
            if (p.isCop()) {
                return isPlayerACop(xpos, ypos) ? "ALLY " : "ENEMY ";
            } else {
                if (!robbers.get(p).equals("CAUGHT") && !p.isDead()) {
                    return isPlayerACop(xpos, ypos) ? "ENEMY " : "ALLY ";
                }
            }
        }
        if (grid.hasGold(xpos + x, ypos + y)) {
            return "GOLD ";
        }
        return "EMPTY ";
    }

    public int getRobberCount() {
        return robberCount;
    }

    public void decreaseRobberCount() {
        robberCount--;
    }
}
