package com.example.goldfinder.client;

import com.example.goldfinder.client.commands.IClientCommand;
import com.example.goldfinder.client.commands.SurroundingClient;
import com.example.utils.ConnectionMode;
import com.example.goldfinder.client.commands.ClientCommandParser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class ClientBoi extends IClient {
    private boolean isConnected = false;
    private boolean isPlaying = false;

    public IClientCommand updateClient(){
        if(!isConnected) return null;
        String command = receiveMessage(mode);
        if(command.isEmpty()) return null;

        System.out.println("Received command : " + command);
        return ClientCommandParser.parseCommand(command);
    }

    @Override
    public String updateSurrounding(int xpos, int ypos) {
        if (isPlaying) return sendCommand(new SurroundingClient(), "");
        return "";
    }

    public String sendCommand(IClientCommand command, String params) {
        if(!isConnected) isConnected = true;

        System.out.println("Sending command : " + command.getName() + " " + params);
        command.run(this, params);
        String resp = "";
        while (true) {
            if (!(resp = receiveMessage(mode)).isEmpty()) {
                return command.response(this, resp);
            }
        }
    }

    public void redirect(InetSocketAddress address) {
        try {
            tcpSocket.close();
            udpSocket.close();
            if(mode == ConnectionMode.TCP) tcpSocket = SocketChannel.open(address);
            else udpSocket = udpSocket.bind(address);
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
}
