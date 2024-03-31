package com.example.goldfinder.client;

import com.example.goldfinder.client.commands.ClientCommandParser;
import com.example.goldfinder.client.commands.IClientCommand;
import com.example.goldfinder.client.commands.SurroundingClient;
import com.example.utils.ConnectionMode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

public class ClientBoi extends IClient {
    private boolean isConnected = false;
    private boolean isPlaying = false;

    public IClientCommand updateClient() {
        if (!isConnected) return null;
        String command = receiveMessage(mode);
        if (command.isEmpty()) return null;

        System.out.println("Received command : " + command);
        return ClientCommandParser.parseCommand(command);
    }

    @Override
    public String updateSurrounding(int xpos, int ypos) {
        if (isPlaying) return sendCommand(new SurroundingClient(), "");
        return "";
    }

    public String sendCommand(IClientCommand command, String params) {
        System.out.println("Sending command : " + command.getName() + " " + params);
        command.run(this, params);
        String resp;
        while (true) {
            if (!(resp = receiveMessage(mode)).isEmpty()) {
                return command.response(this, resp);
            }
        }
    }

    public void redirect(InetSocketAddress address) {
        try {
            if (mode == ConnectionMode.TCP) {
                tcpSocket.close();
                tcpSocket = SocketChannel.open(address);
                tcpSocket.configureBlocking(false);
            } else {
                udpSocket.close();
                udpSocket = DatagramChannel.open();
                udpSocket.configureBlocking(false);
                udpSocket = udpSocket.connect(address);
            }
            isConnected = true;
        } catch (IOException e) {
            System.out.println("Error while redirecting sockets");
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg) {
        sendMessage(mode, msg);
    }

    public boolean isPlaying() {
        return isPlaying;
    }


    public void setPlaying(boolean b) {
        isPlaying = b;
    }

    public void setConnected(boolean b) {
        isConnected = b;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
