package com.example.goldfinder.client;

import com.example.goldfinder.client.commands.Move_Command;
import com.example.utils.players.PlayerColor;
import javafx.scene.input.KeyEvent;

import java.util.Arrays;

import static com.example.goldfinder.client.Controller.COLUMN_COUNT;
import static com.example.goldfinder.client.Controller.ROW_COUNT;

public class GridViewUpdater {
    public static GridView update(String[] command, GridView gridView, int _row, int _col) {
        if (gridView == null) return null;
        if (command == null) return gridView;

        System.out.println(Arrays.toString(command));

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

    public static String handleKeyEvent(KeyEvent keyEvent, GridView gridView, Controller controller, ClientBoi client){
        String resp = "";

        /*InputContext context = InputContext.getInstance();
        context.getLocale().toString().startsWith("fr") -->azerty*/

        switch (keyEvent.getCode()) {
            case Z,W -> {
                if ((resp = client.sendCommand(new Move_Command(), "UP")).startsWith("VALID_MOVE")) {
                    controller.row = Math.max(0, controller.row - 1);
                    controller.vParallax++;
                    gridView.emptyPlayers();
                }
            }
            case Q,A -> {
                if ((resp = client.sendCommand(new Move_Command(), "LEFT")).startsWith("VALID_MOVE")){
                    controller.column = Math.max(0, controller.column - 1);
                    controller.hParallax++;
                    gridView.emptyPlayers();
                }
            }
            case S -> {
                if ((resp = client.sendCommand(new Move_Command(), "DOWN")).startsWith("VALID_MOVE")){
                    controller.row = Math.min(ROW_COUNT - 1, controller.row + 1);
                    controller.vParallax--;
                    gridView.emptyPlayers();

                }
            }
            case D -> {
                if ((resp = client.sendCommand(new Move_Command(), "RIGHT")).startsWith("VALID_MOVE")) {
                    controller.column = Math.min(COLUMN_COUNT - 1, controller.column + 1);
                    controller.hParallax--;
                    gridView.emptyPlayers();

                }
            }
            default -> {
                return "";
            }
        }
        return resp;
    }
}
