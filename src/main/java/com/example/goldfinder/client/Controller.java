package com.example.goldfinder.client;

import com.example.goldfinder.client.commands.Client_Join;
import com.example.goldfinder.client.commands.Move_Command;
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
    @FXML
    TextField debugCommand;

    GridView gridView;
    int column, row;
    ClientBoi client;

    public void initialize() {
        this.gridView = new GridView(gridCanvas, COLUMN_COUNT, ROW_COUNT);
        client = new ClientBoi(ConnectionMode.TCP);
        score.setText("0");
        gridView.repaint();
        column = 10;
        row = 10;
        gridView.paintToken(column, row);
    }

    public void playToggleButtonAction(ActionEvent actionEvent) {
        String name = playerName.getText();
        if (!client.isPlaying()) {
            if (!name.isEmpty()) {
                String r = client.sendCommand(new Client_Join(), name);
                System.out.println("Response to game_join : " + r);
                playerName.setDisable(true);
                System.out.println("isPlaying = " + client.isPlaying());
            }
        }
    }

    public void restartButtonAction(ActionEvent actionEvent) {
        //TODO:Restart game on this press
        String command = debugCommand.getText();
        if (!command.isEmpty()) {
            client.sendMessage(command);
        }
    }

    public void handleMove(KeyEvent keyEvent) {
        String resp = "";
        switch (keyEvent.getCode()) {
            case W -> {
                if ((resp = client.sendCommand(new Move_Command(), "UP")).startsWith("VALID_MOVE"))
                    row = Math.max(0, row - 1);
            }
            case A -> {
                if ((resp = client.sendCommand(new Move_Command(), "LEFT")).startsWith("VALID_MOVE"))
                    column = Math.max(0, column - 1);
            }
            case S -> {
                if ((resp = client.sendCommand(new Move_Command(), "DOWN")).startsWith("VALID_MOVE"))
                    row = Math.min(ROW_COUNT - 1, row + 1);
            }
            case D -> {
                if ((resp = client.sendCommand(new Move_Command(), "RIGHT")).startsWith("VALID_MOVE"))
                    column = Math.min(COLUMN_COUNT - 1, column + 1);
            }
            default -> {
                return;
            }
        }
        if (resp.endsWith("GOLD")) {
            gridView.goldAt[column][row] = false;
            score.setText(String.valueOf(Integer.parseInt(score.getText()) + 1));
        }
        gridView.repaint();
        gridView.paintToken(column, row);
    }
}

