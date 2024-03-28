package com.example.goldfinder.client;

import com.example.goldfinder.client.commands.Client_Join;
import com.example.goldfinder.client.commands.IClientCommand;
import com.example.goldfinder.client.commands.Move_Command;
import com.example.goldfinder.server.AppServer;
import com.example.utils.ConnectionMode;
import com.example.utils.GameType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class Controller {

    @FXML
    Canvas gridCanvas;
    @FXML
    Label score;
    @FXML
    ChoiceBox<String> connectionMode;
    @FXML
    ChoiceBox<String> gameType;
    @FXML
    TextField playerName;
    @FXML
    TextField debugCommand;
    GridView gridView;
    ClientBoi client;
    int vParallax = 0;
    int hParallax = 0;
    int column, row;
    public static int COLUMN_COUNT = AppServer.COLUMN_COUNT * 2;
    public static int ROW_COUNT = AppServer.ROW_COUNT * 2;

    public void initialize() {
        this.gridView = new GridView(gridCanvas, COLUMN_COUNT, ROW_COUNT);
        client = new ClientBoi();

        score.setText("0");

        gridView.repaint(hParallax, vParallax);

        column = COLUMN_COUNT / 2;
        row = ROW_COUNT / 2;

        gridView.paintPlayer(column, row);

        initConnectionMode();
        initGameType();
        initTimeline();
    }


    private void initTimeline(){
        Timeline timeline = new Timeline();
        KeyFrame kf = new KeyFrame(javafx.util.Duration.seconds(0.1), this::updateClient);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    private void initConnectionMode(){
        connectionMode.getItems().add("TCP");
        connectionMode.getItems().add("UDP");
        connectionMode.setValue("TCP");
    }

    private void initGameType(){
        gameType.getItems().add("SOLO");
        gameType.getItems().add("MULTIPLAYER");
        gameType.setValue("MULTIPLAYER");
    }

    private void initGameMode(){
        gameType.getItems().add("GOLD_FINDER");
        gameType.getItems().add("COPS_AND_ROBBERS");
        gameType.setValue("GOLD_FINDER");
    }

    public void playToggleButtonAction() {
        String name = playerName.getText();
        if (!client.isPlaying()) {
            if (!name.isEmpty()) {
                client.changeConnection(ConnectionMode.valueOf(connectionMode.getValue()));
                client.setGameType(GameType.valueOf(gameType.getValue()));
                client.connect();
                String z = client.sendCommand(new Client_Join(), name);
                String r = client.sendCommand(new Client_Join(), name);
                System.out.println("Response to game_join : " + r);
                playerName.setDisable(true);
                connectionMode.setDisable(true);
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
        int _col = column;
        int _row = row;

        IClientCommand inc_command = client.updateClient();
        if(inc_command != null) inc_command.run(client, "");

        String resp = client.updateSurrounding(_col, _row);
        GridViewUpdater.update(resp.split(" "), gridView, _row, _col);
        gridView.repaint(hParallax, vParallax);
        gridView.paintPlayer(COLUMN_COUNT/2, ROW_COUNT/2);
    }

    public void handleMove(KeyEvent keyEvent) {
        if (!client.isPlaying()) return;
        String resp = "";
        switch (keyEvent.getCode()) {
            case Z -> {
                if ((resp = client.sendCommand(new Move_Command(), "UP")).startsWith("VALID_MOVE")) {
                    row = Math.max(0, row - 1);
                    vParallax++;
                    gridView.emptyPlayers();
                }
            }
            case Q -> {
                if ((resp = client.sendCommand(new Move_Command(), "LEFT")).startsWith("VALID_MOVE")){
                    column = Math.max(0, column - 1);
                    hParallax++;
                    gridView.emptyPlayers();
                }
            }
            case S -> {
                if ((resp = client.sendCommand(new Move_Command(), "DOWN")).startsWith("VALID_MOVE")){
                    row = Math.min(ROW_COUNT - 1, row + 1);
                    vParallax--;
                    gridView.emptyPlayers();

                }
            }
            case D -> {
                if ((resp = client.sendCommand(new Move_Command(), "RIGHT")).startsWith("VALID_MOVE")) {
                    column = Math.min(COLUMN_COUNT - 1, column + 1);
                    hParallax--;
                    gridView.emptyPlayers();

                }
            }
            default -> {
                return;
            }
        }
        if (resp.endsWith("GOLD")) {
            gridView.goldAt[column][row] = false;
            score.setText(String.valueOf(Integer.parseInt(score.getText()) + 1));
        }
        gridView.repaint(hParallax, vParallax);
        gridView.paintPlayer(COLUMN_COUNT/2, ROW_COUNT/2);
    }

    public void exitApplication() {
        System.out.println("Exiting...");
        Platform.exit();
    }

}

