package com.example.goldfinder.client;

import com.example.goldfinder.client.commands.Client_Join;
import com.example.goldfinder.client.commands.IClientCommand;
import com.example.goldfinder.client.commands.Move_Command;
import com.example.goldfinder.server.DispatcherServer;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.example.goldfinder.client.Controller.COLUMN_COUNT;
import static com.example.goldfinder.client.Controller.ROW_COUNT;

public class Bot {
    GridView gridView;
    ClientBoi client;
    int vParallax = 0;
    int hParallax = 0;
    int column, row;
    public static int COLUMN_COUNT = DispatcherServer.COLUMN_COUNT * 2;
    public static int ROW_COUNT = DispatcherServer.ROW_COUNT * 2;

    ConnectionMode connectionMode;

    GameType gameType;

    String name;

    int score;

    public Bot(ConnectionMode connectionMode, GameType gameType, String name){
        client = new ClientBoi();
        score = 0;

        column = COLUMN_COUNT / 2;
        row = ROW_COUNT / 2;

        this.connectionMode = connectionMode;
        this.gameType = gameType;
        this.name = name;

        initTimeline();
    }

    private void initTimeline() {
        Timeline timeline = new Timeline();
        KeyFrame kf = new KeyFrame(javafx.util.Duration.seconds(0.1), this::updateClient);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    public void playToggleButtonAction() {
        if (!client.isPlaying()) {
            if (!name.isEmpty()) {
                client.changeConnection(connectionMode);
                client.setGameType(gameType);
                client.connect();
                client.sendCommand(new Client_Join(), name + " " + gameType);
                String r = client.sendCommand(new Client_Join(), name + " " + gameType);
                System.out.println("Response to game_join : " + r);
            }
        }
    }

    public String updateClient(ActionEvent actionEvent) {
        IClientCommand inc_command = client.updateClient();
        if (inc_command != null) inc_command.run(client, "");
        String resp = client.updateSurrounding(column, row);
        return resp;
    }

    public void randomMove(KeyEvent keyEvent) {
        String resp;
        ArrayList<String> directions = new ArrayList<>();
        directions.add("UP");
        directions.add("DOWN");
        directions.add("LEFT");
        directions.add("RIGHT");
        int random = (int) (Math.random() * 4);

        if (!client.isPlaying()) return;

        switch (directions.get(random)) {
            case "UP" -> {
                if ((resp = client.sendCommand(new Move_Command(), "UP")).startsWith("VALID_MOVE")) {
                    row = Math.max(0, row - 1);
                    System.out.println("GOING UP");
                }
            }
            case "LEFT" -> {
                if ((resp = client.sendCommand(new Move_Command(), "LEFT")).startsWith("VALID_MOVE")){
                    column = Math.max(0, column - 1);
                    System.out.println("GOING LEFT");

                }
            }
            case "DOWN" -> {
                if ((resp = client.sendCommand(new Move_Command(), "DOWN")).startsWith("VALID_MOVE")){
                    row = Math.min(ROW_COUNT - 1, row + 1);
                    System.out.printf("GOING DOWN");
                }
            }
            case "RIGHT" -> {
                if ((resp = client.sendCommand(new Move_Command(), "RIGHT")).startsWith("VALID_MOVE")) {
                    column = Math.min(COLUMN_COUNT - 1, column + 1);
                    System.out.println("GOING RIGHT");
                }
            }
            default -> {
                return;
            }
        }

        if (resp.endsWith("GOLD")) {
            gridView.goldAt[column][row] = false;
            score++;
        }
    }


    public void exitApplication() {
        System.out.println("Exiting...");
        Platform.exit();
    }

    public static void main(String[] args) {
        Bot bot = new Bot(ConnectionMode.TCP, GameType.GOLD_FINDER, "Bot");
        bot.playToggleButtonAction();
    }


}

