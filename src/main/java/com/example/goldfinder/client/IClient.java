package com.example.goldfinder.client;

import com.example.goldfinder.ICommon;
import com.example.utils.ConnectionMode;
import com.example.utils.GameType;
import com.example.utils.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

import static java.lang.System.exit;

public abstract class IClient extends ICommon {
    ConnectionMode mode = ConnectionMode.TCP;
    boolean isMultiplayer = true;

    public void connect() {
        Logger.printYellow("Trying to connect to server...");
        try {
            if (mode == ConnectionMode.TCP) startTCPConnection();
            else if (mode == ConnectionMode.UDP) startUDPConnection();
        } catch (IOException e) {
            Logger.printError("Connection failed. Exiting...");
            clean();
        }
        if (tcpSocket != null || udpSocket != null) Logger.printSucess("Connected to server!");
    }

    public void changeConnection(ConnectionMode mode) {
        this.mode = mode;
    }

    public void setGameType(GameType gameType) {
        if (gameType == GameType.SINGLEPLAYER) isMultiplayer = false;
        else isMultiplayer = true;
    }
    public abstract String updateSurrounding(int xpos, int ypos) throws IOException, InterruptedException;

    protected void clean() {
        try {
            if (tcpSocket != null) tcpSocket.close();
            if (udpSocket != null) udpSocket.close();
            exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTCPConnection() {
        int attempts = 0;
        while (tcpSocket == null && attempts < 10) {
            try {
                tcpSocket = SocketChannel.open(new InetSocketAddress("127.0.0.1", 1234));
                tcpSocket.configureBlocking(false);

            } catch (Exception e) {
                attempts++;
                if (attempts == 10) {
                    Logger.printError("Connection failed after 10 tries. Exiting...");
                    clean();
                }
            }
        }
    }

    private void startUDPConnection() throws IOException {
        udpSocket = DatagramChannel.open();
        udpSocket.configureBlocking(false);
        udpSocket.connect(new InetSocketAddress("127.0.0.1", 1234));
    }
}

