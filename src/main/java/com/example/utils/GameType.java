package com.example.utils;

import com.example.goldfinder.server.DispatcherServer;

public enum GameType {
    GOLD_FINDER_MASSIVE(),
    GOLD_FINDER_SOLO(),
    GOLD_FINDER(),
    COPS_AND_ROBBERS(),
    COPS_AND_ROBBERS_MASSIVE();

    GameType() {
    }

    public static String getGameType(String gameType) {
        return switch (gameType) {
            case "GOLD_FINDER_SOLO" -> " GOLD_FINDER_SOLO";
            case "GOLD_FINDER" -> " GOLD_FINDER";
            case "COPS_AND_ROBBERS" -> " COPS_AND_ROBBERS";
            case "COPS_AND_ROBBERS_MASSIVE" -> " COPS_AND_ROBBERS_MASSIVE";
            case "GOLD_FINDER_MASSIVE" -> " GOLD_FINDER_MASSIVE";
            default -> null;
        };
    }

    public static int getGridSize(GameType gameType) {
        return switch (gameType) {
            case GOLD_FINDER_SOLO -> DispatcherServer.ROW_COUNT * 2;
            case GOLD_FINDER, COPS_AND_ROBBERS-> (DispatcherServer.ROW_COUNT * DispatcherServer.DEFAULT_PLAYER_COUNT) * 2;
            case GOLD_FINDER_MASSIVE, COPS_AND_ROBBERS_MASSIVE -> 32*4;
        };
    }


}
