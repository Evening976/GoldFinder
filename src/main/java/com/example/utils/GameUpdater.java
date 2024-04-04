package com.example.utils;

import com.example.goldfinder.server.GameServer;
import com.example.goldfinder.server.commands.gameserver.Game_End;
import com.example.utils.games.AbstractGame;
import com.example.utils.players.AbstractPlayer;
import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;

public class GameUpdater {
    public static String updateGame(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame game, InetSocketAddress addr, String[] params) {
        String dir = "INVALID_MOVE";
        switch (params[0].toUpperCase()) {
            case "UP" -> {
                dir = game.getUp(p.getxPos(), p.getyPos());
                if (game.isValidMove(dir)) {
                    dir = game.updateGame(p, dir, 0, -1);
                    if(game.hasEnded() || dir.contains("GAME_END")){
                        endGame(client, server, p, game, addr);
                    }
                }
            }
            case "DOWN" -> {
                dir = game.getDown(p.getxPos(), p.getyPos());
                if (game.isValidMove(dir)) {
                    dir = game.updateGame(p, dir, 0, 1);
                    if(game.hasEnded() || dir.contains("GAME_END")) {
                        endGame(client, server, p, game, addr);
                    }
                }
            }
            case "LEFT" -> {
                dir = game.getLeft(p.getxPos(), p.getyPos());
                if (game.isValidMove(dir)) {
                    System.out.println(dir);
                    dir = game.updateGame(p, dir, -1, 0);
                    if(dir.contains("INVALID_MOVE")){
                        return "INVALID_MOVE";
                    }
                    if(dir.contains("GAME_END")){
                        endGame(client, server, p, game, addr);
                        game.endGame();
                    }
                }
            }
            case "RIGHT" -> {
                dir = game.getRight(p.getxPos(), p.getyPos());
                if (game.isValidMove(dir)) {
                    dir = game.updateGame(p, dir, 1, 0);
                    if(dir.contains("GAME_END")){
                        endGame(client, server, p, game, addr);
                        game.endGame();
                    }
                }
            }
        }

        if (game.hasEnded() || dir.contains("GAME_END")) {
            Thread t = new Thread(() -> server.saveScore(server.getGames().saveScores(p.getGameID())));
            t.start();
            return new Game_End().run(client, server, p, game, addr, null);
        } else if (!dir.endsWith("WALL ") && !dir.contains("PLAYER") && !dir.endsWith("ENEMY ") && !dir.endsWith("ALLY ")) {
        } else if (!dir.endsWith("WALL ") && !dir.contains("PLAYER") && !dir.endsWith("ENEMY ") && !dir.endsWith("ALLY ") && !dir.contains("INVALID")) {
            return "VALID_MOVE:" + dir.stripTrailing().replace(dir.split(":")[0] + ": ", "");
        }

        return "INVALID_MOVE";
    }

    private static void endGame(SelectableChannel client, GameServer server, AbstractPlayer p, AbstractGame game, InetSocketAddress addr) {
        for (AbstractPlayer abstractPlayer : game.getPlayers()) {
            server.sendMessage(abstractPlayer.getClient(),
                    new Game_End().run(client, server, p, game, addr, null),
                    abstractPlayer.getAddress());
        }
    }
}
