package com.example.goldfinder.client;

import com.example.utils.players.PlayerColor;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.Map;

public class GridView {
    Canvas canvas;
    int columnCount, rowCount;
    Map<Pair<Integer, Integer>, Color> playerPositions = new java.util.HashMap<>();
    boolean[][] goldAt, vWall, hWall;

    public GridView(Canvas canvas, int columnCount, int rowCount) {
        this.canvas = canvas;
        this.columnCount = columnCount += 1;
        this.rowCount = rowCount += 1;
        goldAt = new boolean[columnCount][rowCount];
        vWall = new boolean[columnCount + 1][rowCount];
        hWall = new boolean[columnCount][rowCount + 1];
    }

    public void repaint(int hParallax, int vParallax) {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (Pair<Integer, Integer> player : playerPositions.keySet()) {
            System.out.println("Painting player at " + player.getKey() + " " + player.getValue() + " " + playerPositions.get(player));
            canvas.getGraphicsContext2D().setFill(playerPositions.get(player));
            canvas.getGraphicsContext2D().fillRect((player.getKey() + hParallax) * cellWidth(), (player.getValue() + vParallax) * cellHeight(), cellWidth(), cellHeight());
        }
        for (int column = 0; column < columnCount; column++) {
            for (int row = 0; row < rowCount; row++) {
                if (goldAt[column][row]) {
                    canvas.getGraphicsContext2D().setFill(Color.YELLOW);
                    canvas.getGraphicsContext2D().fillOval((column + hParallax) * cellWidth(), (row + vParallax) * cellHeight(), cellWidth(), cellHeight());
                }
            }
        }

        canvas.getGraphicsContext2D().setStroke(Color.WHITE);
        for (int column = 0; column < columnCount; column++) {
            for (int row = 0; row < rowCount; row++) {
                if (vWall[column][row]) {
                    canvas.getGraphicsContext2D().strokeLine((column + hParallax) * cellWidth(), (row + vParallax) * cellHeight(), (column + hParallax) * cellWidth(), (vParallax + row + 1) * cellHeight());
                }
                if (hWall[column][row]) {
                    canvas.getGraphicsContext2D().strokeLine((column + hParallax) * cellWidth(), (row + vParallax) * cellHeight(), (hParallax + column + 1) * cellWidth(), (row + vParallax) * cellHeight());
                }
            }
        }
    }

    public void setGoldAt(int col, int row) {
        if (col >= 0 && col < columnCount && row >= 0 && row < rowCount)
            goldAt[col][row] = true;
    }

    public void setVWall(int col, int row) {
        if (col >= 0 && col < columnCount + 1 && row >= 0 && row < rowCount) {
            if (!vWall[col][row]) {
                System.out.println("Setting hWall at " + col + " " + row);
            }
            vWall[col][row] = true;
        }
    }

    public void setHWall(int col, int row) {
        if (col >= 0 && col < columnCount && row >= 0 && row < rowCount + 1) {
            if (!hWall[col][row]) {
                System.out.println("Setting hWall at " + col + " " + row);
            }
            hWall[col][row] = true;
        }
    }

    private int cellWidth() {
        return (int) canvas.getWidth() / columnCount;
    }

    private int cellHeight() {
        return (int) canvas.getWidth() / rowCount;
    }

    public void emptyPlayers() {
        playerPositions.clear();
    }

    public void setEmpty(int col, int row) {
        goldAt[col][row] = false;
        if (playerPositions.get(new Pair<>(col, row)) != null) {
            playerPositions.remove(new Pair<>(col, row));
        }
    }

    public void setPlayerPositions(int col, int row, int playerIndex) {
        playerPositions.put(new Pair<>(col, row), PlayerColor.values()[playerIndex%6].getColor());
    }

    public void setPlayerPositions(int col, int row, Color playerColor) {

        playerPositions.put(new Pair<>(col, row), playerColor);
    }

    public void paintPlayer(int column, int row) {
        canvas.getGraphicsContext2D().setFill(Color.BLUE);
        canvas.getGraphicsContext2D().fillRect(row * cellWidth() + 2, column * cellHeight() + 2, cellWidth() - 4, cellHeight() - 4);
    }

}
