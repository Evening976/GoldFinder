package com.example.goldfinder.client;

import com.example.goldfinder.client.commands.Client_Join;
import com.example.goldfinder.client.commands.IClientCommand;
import com.example.goldfinder.client.commands.Move_Command;
import com.example.goldfinder.server.DispatcherServer;
import com.example.utils.ConnectionMode;
import com.example.utils.GameType;

import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bot {
    ClientBoi client;
    int column, row;
    public static int COLUMN_COUNT = DispatcherServer.COLUMN_COUNT * 2;
    public static int ROW_COUNT = DispatcherServer.ROW_COUNT * 2;

    ConnectionMode connectionMode;
    GameType gameType;
    String name;


    public Bot(ConnectionMode connectionMode, GameType gameType, String name){
        client = new ClientBoi();

        column = COLUMN_COUNT / 2;
        row = ROW_COUNT / 2;

        this.connectionMode = connectionMode;
        this.gameType = gameType;
        this.name = name;

    }

    public void startBot() {
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

    public void run() {
        System.out.println("bro is wating");
        IClientCommand inc_command = client.updateClient();
        if (inc_command != null) inc_command.run(client, "");

        if (!client.isPlaying()) return;

        System.out.println("running");
        String resp;
        ArrayList<String> directions = new ArrayList<>();
        directions.add("UP");
        directions.add("DOWN");
        directions.add("LEFT");
        directions.add("RIGHT");
        int random = (int) (Math.random() * 4);


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
    }

    public static void main(String[] args) {
        Bot bot = new Bot(ConnectionMode.TCP, GameType.GOLD_FINDER, "Bot");
        bot.startBot();
        System.out.println("waiting for game to start");
        //while (!bot.client.isPlaying()) {}
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(bot::run, 2, 2, TimeUnit.SECONDS);


    }


}

