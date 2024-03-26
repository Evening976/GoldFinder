package com.example.goldfinder.client;

import com.example.goldfinder.client.commands.Client_Join;
import com.example.goldfinder.client.commands.Move_Command;
import com.example.utils.ConnectionMode;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
    private Timeline timeline;

    @FXML
    TextField playerName;
    @FXML
    TextField debugCommand;

    GridView gridView;
    int column, row;
    ClientBoi client;

    int vParallax = 0;
    int hParallax = 0;

    public void initialize() {
        this.gridView = new GridView(gridCanvas, COLUMN_COUNT, ROW_COUNT);

        timeline = new Timeline();
        KeyFrame kf = new KeyFrame(javafx.util.Duration.seconds(0.1), this::updateClient);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(kf);
        client = new ClientBoi(ConnectionMode.TCP);
        score.setText("0");
        gridView.repaint();
        column = 10;
        row = 10;
        gridView.paintPlayer(column, row, 0);
        timeline.play();
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

    public void updateClient(ActionEvent actionEvent) {
        if (!client.isPlaying()) return;
        int _col = column;
        int _row = row; //pour éviter les problèmes de concurrence
        String resp = client.updateClient(_col, _row);
        System.out.println(resp);
        String[] parts = resp.split(" ");
        for (String p : parts) {
            String[] subparts = p.split(":");
            switch (subparts[0]) {
                case "up" -> {
                    if (subparts[1].equals("WALL")) {
                        gridView.setHWall(_col, _row);
                    } else if (subparts[1].equals("GOLD")) {
                        gridView.setGoldAt(_col, _row - 1);
                    } else if (subparts[1].startsWith("PLAYER")) {
                        gridView.paintPlayer(_col, _row - 1, Integer.parseInt(subparts[1].substring(6)));
                    }
                }
                case "down" -> {
                    if (subparts[1].equals("WALL")) {
                        gridView.setHWall(_col, _row + 1);
                    } else if (subparts[1].equals("GOLD")) {
                        gridView.setGoldAt(_col,_row + 1);
                    } else if (subparts[1].startsWith("PLAYER")) {
                        gridView.paintPlayer(_col, _row + 1, Integer.parseInt(subparts[1].substring(6)));
                    }
                }
                case "left" -> {
                    if (subparts[1].equals("WALL")) {
                        gridView.setVWall(_col, _row);
                    } else if (subparts[1].equals("GOLD")) {
                        gridView.setGoldAt(_col - 1, _row);
                    } else if (subparts[1].startsWith("PLAYER")) {
                        gridView.paintPlayer(_col - 1, _row, Integer.parseInt(subparts[1].substring(6)));
                    }
                }
                case "right" -> {
                    if (subparts[1].equals("WALL")) {
                        gridView.setVWall(_col + 1, _row);
                    } else if (subparts[1].equals("GOLD")) {
                        gridView.setGoldAt(_col + 1, _row);
                    } else if (subparts[1].startsWith("PLAYER")) {
                        gridView.paintPlayer(_col + 1, _row, Integer.parseInt(subparts[1].substring(6)));
                    }
                }
            }
        }
        gridView.repaint();
        gridView.paintPlayer(column, row, 0);
    }

    public void handleMove(KeyEvent keyEvent) {
        String resp = "";
        if (!client.isPlaying()) return;
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
        gridView.paintPlayer(column, row, 0);
    }
}

