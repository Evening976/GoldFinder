package com.example.utils;

import com.example.goldfinder.server.GameServer;
import com.example.goldfinder.server.commands.gameserver.Game_End;
import com.example.utils.games.AbstractGame;
import com.example.utils.games.CRGame;
import com.example.utils.games.GFGame;
import com.example.utils.players.AbstractPlayer;
import com.example.utils.players.CRPlayer;
import com.example.utils.players.GFPlayer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;

public class GameUpdater {
    public static String updateGame(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame game, InetSocketAddress addr, String[] params) {
        String dir = "INVALID_MOVE";
        switch (params[0].toUpperCase()) {
            case "UP" -> {
                dir = game.getUp(p.getxPos(), p.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD") || dir.contains("ENEMY")) {
                    if (game instanceof CRGame || p instanceof CRPlayer) {
                        if (dir.contains("ENEMY")) {
                            ((CRGame) game).catchRobber(p, game.getPlayerFromCoordinates(p.getxPos(), p.getyPos() - 1));
                            handleCREnd(client, server, p, game, addr, dir);
                        } else {
                            game.movePlayer(p, 0, -1);
                            handleCREnd(client, server, p, game, addr, dir);
                        }
                    } else if (game instanceof GFGame || p instanceof GFPlayer) {
                        game.movePlayer(p, 0, -1);
                        handleGFEnd(client, server, p, game, addr, dir);
                    }
                }
            }
            case "DOWN" -> {
                dir = game.getDown(p.getxPos(), p.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD") || dir.contains("ENEMY")) {
                    if (game instanceof CRGame || p instanceof CRPlayer) {
                        if (dir.contains("ENEMY")) {
                            ((CRGame) game).catchRobber(p, game.getPlayerFromCoordinates(p.getxPos(), p.getyPos() + 1));
                            handleCREnd(client, server, p, game, addr, dir);
                        } else {
                            game.movePlayer(p, 0, 1);
                            handleCREnd(client, server, p, game, addr, dir);
                        }
                    } else if (game instanceof GFGame || p instanceof GFPlayer) {
                        game.movePlayer(p, 0, 1);
                        handleGFEnd(client, server, p, game, addr, dir);
                    }
                }
            }
            case "LEFT" -> {
                dir = game.getLeft(p.getxPos(), p.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD") || dir.contains("ENEMY")) {
                    if (game instanceof CRGame || p instanceof CRPlayer) {
                        if (dir.contains("ENEMY")) {
                            ((CRGame) game).catchRobber(p, game.getPlayerFromCoordinates(p.getxPos() - 1, p.getyPos()));
                            handleCREnd(client, server, p, game, addr, dir);
                        } else {
                            game.movePlayer(p, -1, 0);
                            handleCREnd(client, server, p, game, addr, dir);
                        }
                    } else if (game instanceof GFGame || p instanceof GFPlayer) {
                        game.movePlayer(p, -1, 0);
                        handleGFEnd(client, server, p, game, addr, dir);
                    }
                }
            }
            case "RIGHT" -> {
                dir = game.getRight(p.getxPos(), p.getyPos());
                if (dir.contains("EMPTY") || dir.contains("GOLD") || dir.contains("ENEMY")) {
                    if (game instanceof CRGame || p instanceof CRPlayer) {
                        if (dir.contains("ENEMY")) {
                            ((CRGame) game).catchRobber(p, game.getPlayerFromCoordinates(p.getxPos() + 1, p.getyPos()));
                            handleCREnd(client, server, p, game, addr, dir);
                        } else {
                            game.movePlayer(p, 1, 0);
                            handleCREnd(client, server, p, game, addr, dir);
                        }
                    } else if (game instanceof GFGame || p instanceof GFPlayer) {
                        game.movePlayer(p, 1, 0);
                        handleGFEnd(client, server, p, game, addr, dir);
                    }
                }
            }
        }

        if (game.hasEnded()) {
            return "GAME_END";
        }

        if (!dir.endsWith("WALL ") && !dir.contains("PLAYER") && !dir.contains("ENEMY ") && !dir.contains("ALLY ")) {
            System.out.println("VALID_MOVE:" + dir.stripTrailing().replace(dir.split(":")[0] + ": ", ""));
            return "VALID_MOVE:" + dir.stripTrailing().replace(dir.split(":")[0] + ": ", "");
        }

        return "INVALID_MOVE";
    }

    private static void handleGFEnd(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame game, InetSocketAddress addr, String dir) {
        assert game instanceof GFGame;
        System.out.println(p.getxPos() + " " + p.getyPos() + " " + dir);
        System.out.println(game.getPlayer(p).getxPos() + " " + game.getPlayer(p).getyPos() + " " + dir);
        ((GFGame) game).setDiscoveredCell(p.getxPos(), p.getyPos());
        if (dir.contains("GOLD")) {
            game.collectGold(p);
        }
        if (((GFGame) game).getMaxCells() == ((GFGame) game).getDiscoveredCells()) {
            endGame(client, server, p, game, addr);
        }
    }

    private static void handleCREnd(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame game, InetSocketAddress addr, String dir) {
        assert game instanceof CRGame;

        if (dir.contains("GOLD")) {
            System.out.println("Gold collected!");
            game.collectGold(p);
            if (((CRGame) game).getGoldCount() == 0) {
                endGame(client, server, p, game, addr);
            }
        }

        for (AbstractPlayer robber : ((CRGame) game).getRobbers().keySet()) {
            if (((CRGame) game).getRobbers().get(robber).equals("CAUGHT") && game.getPlayers().contains(robber)) {
                ((CRGame) game).decreaseRobberCount();
                server.sendMessage(robber.getClient(), new Game_End().run(client, server, p, game, addr, null), robber.getAddress());
                game.removePlayer(robber);
            }
        }

        if (((CRGame) game).getRobberCount() == 0) {
            for (AbstractPlayer abstractPlayer : game.getPlayers()) {
                System.out.println("Game ended COPS WIN!");
                server.sendMessage(abstractPlayer.getClient(),
                        new Game_End().run(client, server, p, game, addr, null), abstractPlayer.getAddress());
                game.setHasEnded(true);
            }
        }
    }

    private static void endGame(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame game, InetSocketAddress addr) {
        for (AbstractPlayer abstractPlayer : game.getPlayers()) {
            System.out.println("Game ended");
            server.sendMessage(abstractPlayer.getClient(),
                    new Game_End().run(client, server, p, game, addr, null),
                    abstractPlayer.getAddress());
            game.setHasEnded(true);
        }
    }
}
