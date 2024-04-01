package com.example.utils;

public enum GameType {
    GOLD_FINDER_MASSIVE(),
    GOLD_FINDER_SOLO(),
    GOLD_FINDER(),
    COPS_AND_ROBBERS();

    GameType() {
    }

    public static String getGameType(String gameType) {
        return switch (gameType) {
            case "GOLD_FINDER_SOLO" -> " GOLD_FINDER_SOLO";
            case "GOLD_FINDER" -> " GOLD_FINDER";
            case "COPS_AND_ROBBERS" -> " COPS_AND_ROBBERS";
            case "GOLD_FINDER_MASSIVE" -> " GOLD_FINDER_MASSIVE";
            default -> null;
        };
    }

    public static int getGridSize(GameType gameType) {
        return switch (gameType) {
            case GOLD_FINDER_SOLO -> 20;
            case GOLD_FINDER -> 20;
            case COPS_AND_ROBBERS -> 20;
            case GOLD_FINDER_MASSIVE -> 80;
        };
    }


}
