package com.example.goldfinder.client;

import com.example.utils.Players.PlayerColor;

public class GridViewUpdater {
    public static GridView update(String[] command, GridView gridView, int _row, int _col) {
        if (gridView == null) return null;
        if (command == null) return gridView;

        for (String p : command) {
            String[] subparts = p.split(":");
            switch (subparts[0]) {
                case "up" -> {
                    if (subparts[1].equals("WALL")) {
                        gridView.setHWall(_col, _row);
                    } else if (subparts[1].equals("GOLD")) {
                        gridView.setGoldAt(_col, _row - 1);
                    } else if (subparts[1].startsWith("PLAYER")) {
                        gridView.setPlayerPositions(_col, _row - 1, PlayerColor.values()[Integer.parseInt(subparts[1].substring(subparts[1].length() - 1))]);
                    } else if (subparts[1].equals("EMPTY")) {
                       gridView.setEmpty(_col, _row - 1);
                    }
                }
                case "down" -> {
                    if (subparts[1].equals("WALL")) {
                        gridView.setHWall(_col, _row + 1);
                    } else if (subparts[1].equals("GOLD")) {
                        gridView.setGoldAt(_col, _row + 1);
                    } else if (subparts[1].startsWith("PLAYER")) {
                        gridView.setPlayerPositions(_col, _row + 1, PlayerColor.values()[Integer.parseInt(subparts[1].substring(subparts[1].length() - 1))]);
                    } else if (subparts[1].equals("EMPTY")) {
                        gridView.setEmpty(_col, _row + 1);
                    }
                }
                case "left" -> {
                    if (subparts[1].equals("WALL")) {
                        gridView.setVWall(_col, _row);
                    } else if (subparts[1].equals("GOLD")) {
                        gridView.setGoldAt(_col - 1, _row);
                    } else if (subparts[1].startsWith("PLAYER")) {
                        gridView.setPlayerPositions(_col - 1, _row, PlayerColor.values()[Integer.parseInt(subparts[1].substring(subparts[1].length() - 1))]);
                    } else if (subparts[1].equals("EMPTY")) {
                        gridView.setEmpty(_col - 1, _row);
                    }
                }
                case "right" -> {
                    if (subparts[1].equals("WALL")) {
                        gridView.setVWall(_col + 1, _row);
                    } else if (subparts[1].equals("GOLD")) {
                        gridView.setGoldAt(_col + 1, _row);
                    } else if (subparts[1].startsWith("PLAYER")) {
                        gridView.setPlayerPositions(_col + 1, _row, PlayerColor.values()[Integer.parseInt(subparts[1].substring(subparts[1].length() - 1))]);
                    } else if (subparts[1].equals("EMPTY")) {
                        gridView.setEmpty(_col + 1, _row);
                    }
                }
            }
        }
        return gridView;
    }
}
