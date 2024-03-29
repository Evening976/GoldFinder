package com.example.utils;

public enum GameType {
    GOLD_FINDER_SOLO("GOLD_FINDER_SOLO"),
    GOLD_FINDER("GOLD_FINDER"),
    COPS_AND_ROBBERS("COPS_AND_ROBBERS");

    GameType(String goldFinderSolo) {

    }

    public static String getGameType(String gameType) {
        return switch (gameType) {
            case "GOLD_FINDER_SOLO" -> " GOLD_FINDER_SOLO";
            case "GOLD_FINDER" -> " GOLD_FINDER";
            case "COPS_AND_ROBBERS" -> " COPS_AND_ROBBERS";
            default -> null;
        };
    }


}
