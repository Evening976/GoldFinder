package com.example.goldfinder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

import static com.example.goldfinder.server.AppServer.COLUMN_COUNT;
import static com.example.goldfinder.server.AppServer.ROW_COUNT;

public class Controller {

    @FXML
    Canvas gridCanvas;
    @FXML
    Label score;

    GridView gridView;
    int column, row;

    public void initialize() {
        this.gridView = new GridView(gridCanvas, COLUMN_COUNT, ROW_COUNT);
        score.setText("0");
        gridView.repaint();
        column = 10; row = 10;
        gridView.paintToken(column, row);
    }

    public void pauseToggleButtonAction(ActionEvent actionEvent) {
    }

    public void playToggleButtonAction(ActionEvent actionEvent) {
    }

    public void oneStepButtonAction(ActionEvent actionEvent) {
    }

    public void restartButtonAction(ActionEvent actionEvent) {
    }

    public void handleMove(KeyEvent keyEvent) {
            switch (keyEvent.getCode()) {
                case Z -> row = Math.max(0, row - 1);
                case Q -> column = Math.max(0, column - 1);
                case S -> row = Math.min(ROW_COUNT-1, row + 1);
                case D -> column = Math.min(COLUMN_COUNT-1, column +1);
            }
            gridView.repaint();
            gridView.paintToken(column, row);
    }
}

