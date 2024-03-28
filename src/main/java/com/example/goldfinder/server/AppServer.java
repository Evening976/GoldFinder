package com.example.goldfinder.server;

import java.io.IOException;
import java.util.Random;

public class AppServer extends GameServer {

    public AppServer(int port) throws IOException {
        super(port);
    }


    public static final int ROW_COUNT = 20;
    public static final int COLUMN_COUNT = 20;
    final static int serverPort = 1234;

    public static void main(String[] args) {
        AppServer server;
        Grid grid = new Grid(COLUMN_COUNT, ROW_COUNT, new Random());
        try {
            server = new AppServer(serverPort);
            System.out.println("server should be listening on port " + serverPort);
            server.startServer();
        } catch (IOException e) {
            System.out.println("Error creating server " + e.getMessage());
        }
    }
}
