package com.example.goldfinder.server.commands.gameserver;

import com.example.goldfinder.server.GameServer;
import com.example.utils.games.*;
import com.example.utils.players.AbstractPlayer;
import com.example.utils.players.CRPlayer;
import com.example.utils.players.GFPlayer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;

public class Dir implements GameServerCommand {
    AbstractPlayer player;
    AbstractGame game;
    @Override
    public String run(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame g, InetSocketAddress addr, String[] params) {
        this.player = p;
        this.game = g;
        String dir = "INVALID_MOVE";
        switch (params[0].toUpperCase()) {
            case "UP" -> {
                dir = game.getUp(p.getxPos(), p.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD") || dir.contains("ENEMY")) {
                    if (game instanceof CRGame || p instanceof CRPlayer) {
                        if (dir.contains("ENEMY")) {
                            if (((CRPlayer) p).isCop()) {
                                ((CRGame) game).catchRobber(p, game.getPlayerFromCoordinates(p.getxPos(), p.getyPos() - 1));
                                handleCREnd(client, server, p, g, addr, dir);
                            }
                        } else{
                            game.movePlayer(p, 0, -1);
                            handleCREnd(client, server, p, g, addr, dir);
                        }
                    }
                    else if (game instanceof GFGame || p instanceof GFPlayer) {
                        game.movePlayer(p, 0, -1);
                        handleGFEnd(client, server, p, g, addr, dir);
                    }
                }
            }
            case "DOWN" -> {
                dir = game.getDown(p.getxPos(), p.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD") || dir.contains("ENEMY")) {
                    if (game instanceof CRGame || p instanceof CRPlayer) {
                        if (dir.contains("ENEMY")) {
                            if (((CRPlayer) p).isCop()) {
                                ((CRGame) game).catchRobber(p, game.getPlayerFromCoordinates(p.getxPos(), p.getyPos() + 1));
                                handleCREnd(client, server, p, g, addr, dir);
                            }
                        } else {
                            game.movePlayer(p, 0, 1);
                            handleCREnd(client, server, p, g, addr, dir);
                        }
                    }
                    else if (game instanceof GFGame || p instanceof GFPlayer) {
                        game.movePlayer(p, 0, 1);
                        handleGFEnd(client, server, p, g, addr, dir);
                    }
                }
            }
            case "LEFT" -> {
                dir = game.getLeft(p.getxPos(), p.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD") || dir.contains("ENEMY")) {
                    if (game instanceof CRGame || p instanceof CRPlayer) {
                        if (dir.contains("ENEMY")) {
                            if (((CRPlayer) p).isCop()) {
                                ((CRGame) game).catchRobber(p, game.getPlayerFromCoordinates(p.getxPos()-1, p.getyPos()));
                                handleCREnd(client, server, p, g, addr, dir);
                            }
                        } else{
                            game.movePlayer(p, -1, 0);
                            handleCREnd(client, server, p, g, addr, dir);
                        }

                    }
                    else if (game instanceof GFGame || p instanceof GFPlayer) {
                        game.movePlayer(p, -1, 0);
                        handleGFEnd(client, server, p, g, addr, dir);
                    }
                }
            }
            case "RIGHT" -> {
                dir = game.getRight(p.getxPos(), p.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD") || dir.contains("ENEMY")) {
                    if (game instanceof CRGame || p instanceof CRPlayer) {
                        if (dir.contains("ENEMY")) {
                            if (((CRPlayer) p).isCop()) {
                                ((CRGame) game).catchRobber(p, game.getPlayerFromCoordinates(p.getxPos() + 1, p.getyPos()));
                                //game.movePlayer(p, 1, 0);
                                handleCREnd(client, server, p, g, addr, dir);
                            }
                        } else{
                            game.movePlayer(p, 1, 0);
                            handleCREnd(client, server, p, g, addr, dir);
                        }
                    }
                    else if (game instanceof GFGame || p instanceof GFPlayer) {
                        game.movePlayer(p, 1, 0);
                        handleGFEnd(client, server, p, g, addr, dir);
                    }
                }
            }
        }

        if(game.hasEnded()){
            return "GAME_END";
        }

        if (!dir.endsWith("WALL ") && !dir.contains("PLAYER") && !dir.contains("ENEMY ") && !dir.contains("ALLY ")){
            System.out.println("VALID_MOVE:" + dir.stripTrailing().replace(dir.split(":")[0] + ": ", ""));
            return "VALID_MOVE:" + dir.stripTrailing().replace(dir.split(":")[0] + ": ", "");
        }
        System.out.println("INVALID_MOVE");
        return "INVALID_MOVE";
    }

    private void handleGFEnd(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame g, InetSocketAddress addr, String dir) {
        System.out.println(p.getxPos() + " " + p.getyPos() + " " + dir);
        System.out.println(g.getPlayer(p).getxPos() + " " + g.getPlayer(p).getyPos() + " " + dir);
        if(game instanceof GFGame || p instanceof GFPlayer) {
            ((GFGame)game).setDiscoveredCell(p.getxPos(), p.getyPos());
            if (dir.contains("GOLD")) {
                game.collectGold((GFPlayer) p);
            }
            if(((GFGame)game).getMaxCells() == ((GFGame)game).getDiscoveredCells()) {
                endGame(client, server, p, g, addr);
            }
        }
    }

    public void handleCREnd(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame g, InetSocketAddress addr, String dir) {
        if(game instanceof CRGame || p instanceof CRPlayer) {
            if (dir.contains("GOLD")) {
                System.out.println("Gold collected!");
                game.collectGold(p);
                if (((CRGame) game).getGoldCount() == 0) {
                    endGame(client, server, p, g, addr);
                }
            }
        }

        int robberCount = ((CRGame)g).getRobbers().size();
        for(AbstractPlayer robber : ((CRGame)g).getRobbers().keySet()) {
            if(((CRGame)g).getRobbers().get(robber).equals("CAUGHT")) {
                robberCount--;
                server.sendMessage(robber.getClient(), "GAME_END ", robber.getAddress());
            }

        }

        if (robberCount == 0) {
            for(AbstractPlayer abstractPlayer : game.getPlayers()) {
                System.out.println("Game ended COPS WIN!");
                server.sendMessage(abstractPlayer.getClient(),
                        new Game_End().run(client, server, p, g, addr, null), abstractPlayer.getAddress());
                game.setHasEnded(true);
            }
        }
    }

    private void endGame(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame g, InetSocketAddress addr) {
        for(AbstractPlayer abstractPlayer : game.getPlayers()) {
            System.out.println("Game ended");
            server.sendMessage(abstractPlayer.getClient(),
                    new Game_End().run(client, server, p, g, addr, null),
                    abstractPlayer.getAddress());
            game.setHasEnded(true);
        }
    }

    @Override
    public AbstractGame getGame() {
        return game;
    }

    @Override
    public AbstractPlayer getPlayer() { return game.getPlayer(player); }
}
