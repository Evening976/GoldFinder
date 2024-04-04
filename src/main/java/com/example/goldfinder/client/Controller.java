package com.example.goldfinder.client;

import com.example.goldfinder.client.commands.Client_Join;
import com.example.goldfinder.client.commands.Client_Leaderboard;
import com.example.goldfinder.client.commands.IClientCommand;
import com.example.goldfinder.server.DispatcherServer;
import com.example.utils.ConnectionMode;
import com.example.utils.GameType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.util.List;

public class Controller {
    @FXML
    ListView<String> viewLeaderboard;
    @FXML
    TextField leaderSizeText;
    @FXML
    Button getleaderboardbtn;
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
    @FXML
    ToggleButton playToggleButton;

    GridView gridView;
    ClientBoi client;
    int vParallax = 0;
    int hParallax = 0;
    int column, row;
    public static int COLUMN_COUNT; // = DispatcherServer.COLUMN_COUNT * 2;
    public static int ROW_COUNT; // = DispatcherServer.ROW_COUNT * 2;

    public void initialize() {
        this.gridView = new GridView(gridCanvas, COLUMN_COUNT, ROW_COUNT);
        client = new ClientBoi();

        score.setText("0");

        gridView.repaint(hParallax, vParallax);

        column = COLUMN_COUNT / 2;
        row = ROW_COUNT / 2;

        gridView.paintPlayer(column, row);

        getleaderboardbtn.setDisable(true);

        initConnectionMode();
        initGameMode();
        initTimeline();
    }

    private void initTimeline() {
        Timeline timeline = new Timeline();
        KeyFrame kf = new KeyFrame(javafx.util.Duration.seconds(0.1), this::updateClient);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    private void initConnectionMode() {
        connectionMode.getItems().add("TCP");
        connectionMode.getItems().add("UDP");
        connectionMode.setValue("TCP");
    }

    private void initGameMode() {
        gameType.getItems().add("GOLD_FINDER");
        gameType.getItems().add("GOLD_FINDER_SOLO");
        gameType.getItems().add("GOLD_FINDER_MASSIVE");
        gameType.getItems().add("COPS_AND_ROBBERS");
        gameType.getItems().add("COPS_AND_ROBBERS_MASSIVE");
        gameType.setValue("GOLD_FINDER");
    }

    public void playToggleButtonAction() {
        String name = playerName.getText();
        if (!client.isPlaying()) {
            if (!client.isConnected()) {
                if (!name.isEmpty()) {
                    client.changeConnection(ConnectionMode.valueOf(connectionMode.getValue()));
                    client.setGameType(GameType.valueOf(gameType.getValue()));
                    client.connect();
                    playToggleButton.setText("Play!");
                    client.sendCommand(new Client_Join(), name + GameType.getGameType(gameType.getValue()));
                    getleaderboardbtn.setDisable(false);
                    connectionMode.setDisable(true);
                    playerName.setDisable(true);
                }
            } else {
                String r = client.sendCommand(new Client_Join(), name + GameType.getGameType(gameType.getValue()));
                System.out.println("Response to game_join : " + r);
                playToggleButton.setDisable(true);
            }
        }
    }

    public void restartButtonAction() {
        client.clean();
        score.setText("0");
        vParallax = 0;
        hParallax = 0;
        column = COLUMN_COUNT / 2;
        row = ROW_COUNT / 2;

        this.gridView = new GridView(gridCanvas, COLUMN_COUNT, ROW_COUNT);
        client = new ClientBoi();

        gridView.repaint(hParallax, vParallax);
        gridView.paintPlayer(column, row);
        getleaderboardbtn.setDisable(true);
        connectionMode.setDisable(false);
        playToggleButton.setDisable(false);
        playToggleButton.setText("Connect");
    }

    public void updateClient(ActionEvent actionEvent) {
        int startUpdate = (int) System.currentTimeMillis();
        IClientCommand inc_command = client.updateClient();
        if (inc_command != null) inc_command.run(client, "");


        String resp = client.updateSurrounding(column, row);
        int endUpdate = (int) System.currentTimeMillis();
        System.out.println("Update time : " + (endUpdate - startUpdate) + " ms");
        gridView = GridViewUpdater.update(resp.split(" "), gridView, row, column);
        gridView.repaint(hParallax, vParallax);
        gridView.paintPlayer(COLUMN_COUNT / 2, ROW_COUNT / 2);
    }

    public void handleMove(KeyEvent keyEvent) {
        if (!client.isPlaying()) return;

        if (GridViewUpdater.handleKeyEvent(keyEvent, gridView, this, client).endsWith("GOLD")) {
            gridView.goldAt[column][row] = false;
            score.setText(String.valueOf(Integer.parseInt(score.getText()) + 1));
        }
        gridView.repaint(hParallax, vParallax);
        gridView.paintPlayer(COLUMN_COUNT / 2, ROW_COUNT / 2);
    }

    public void getLeaderboard() {
        if (!client.isConnected() && !client.isPlaying()) {
            return;
        }

        String size = leaderSizeText.getText();
        if (size.isEmpty()) {
            size = "10";
        }
        String r = client.sendCommand(new Client_Leaderboard(), size);
        viewLeaderboard.getItems().clear();
        List<String> lines = List.of(r.split("\n"));
        ObservableList<String> list = FXCollections.observableArrayList(lines);
        for (String s : lines) {
            viewLeaderboard.getItems().add(s);
        }
        viewLeaderboard.prefHeightProperty().bind(Bindings.size(list).multiply(24));
    }

    public void exitApplication() {
        System.out.println("Exiting...");
        Platform.exit();
    }

    public void gameModeChanged() {
        GameType currentGT = GameType.valueOf(this.gameType.getValue());
        COLUMN_COUNT = GameType.getGridSize(currentGT);
        ROW_COUNT = GameType.getGridSize(currentGT);
        this.gridView = new GridView(gridCanvas, COLUMN_COUNT, ROW_COUNT);
        hParallax = 0;
        vParallax = 0;
        column = COLUMN_COUNT / 2;
        row = ROW_COUNT / 2;
        gridView.repaint(hParallax, vParallax);
        gridView.paintPlayer(column, row);
    }
}

