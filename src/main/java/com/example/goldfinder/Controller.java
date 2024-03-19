package com.example.goldfinder;

import com.example.goldfinder.client.ClientBoi;
import com.example.utils.ConnectionMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import static com.example.goldfinder.server.AppServer.COLUMN_COUNT;
import static com.example.goldfinder.server.AppServer.ROW_COUNT;

public class Controller {

    @FXML
    Canvas gridCanvas;
    @FXML
    Label score;

    @FXML
    TextField playerName;

    GridView gridView;
    int column, row;
    ClientBoi client;

    public void initialize() {
        this.gridView = new GridView(gridCanvas, COLUMN_COUNT, ROW_COUNT);
        client = new ClientBoi(ConnectionMode.TCP);
        score.setText("0");
        gridView.repaint();
        column = 10; row = 10;
        gridView.paintToken(column, row);
    }

    public void playToggleButtonAction(ActionEvent actionEvent) {
        String name = playerName.getText();
        if(!client.isPlaying()){
            if(!name.isEmpty()){
                client.sendMessage("GAME_JOIN " + name);
                client.setPlaying(true);
                playerName.setDisable(true);
            }
        }

    }

    public void restartButtonAction(ActionEvent actionEvent) {
        //TODO:Restart game on this press
    }

    public void handleMove(KeyEvent keyEvent) {
            switch (keyEvent.getCode()) {
                case W -> row = Math.max(0, row - 1);
                case A -> column = Math.max(0, column - 1);
                case S -> row = Math.min(ROW_COUNT-1, row + 1);
                case D -> column = Math.min(COLUMN_COUNT-1, column +1);
            }
            gridView.repaint();
            gridView.paintToken(column, row);
    }
}

