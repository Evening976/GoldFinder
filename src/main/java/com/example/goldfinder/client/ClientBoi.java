package com.example.goldfinder.client;

import com.example.goldfinder.client.commands.IClientCommand;
import com.example.goldfinder.client.commands.SurroundingClient;
import com.example.utils.ConnectionMode;

import java.io.IOException;

public class ClientBoi extends IClient {
    private boolean isPlaying = false;

    public ClientBoi(ConnectionMode mode) {
        super(mode);
        try {
            connect();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String updateClient(int xpos, int ypos) {
        return sendCommand(new SurroundingClient(), "");
    }

    public String sendCommand(IClientCommand command, String params) {
        command.run(this, params);
        //System.out.println("sent command : " + params);
        String resp = "";

        if (mode == ConnectionMode.TCP) {
            while (true) {
                if (!(resp = receiveMessage(tcpSocket)).isEmpty()){return command.response(this, resp);}
            }
        } else {
            while (true) {
                if (!(resp = receiveMessage(udpSocket)).isEmpty()){return command.response(this, resp);}
            }
        }
    }

    public void sendMessage(String msg) {
        if (mode == ConnectionMode.TCP) {
            sendMessage(tcpSocket, Wbuffer, msg);
        } else
            sendMessage(udpSocket, Wbuffer, msg);

        isPlaying = true;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean b) {
        isPlaying = b;
    }
}
