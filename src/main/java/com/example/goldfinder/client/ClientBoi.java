package com.example.goldfinder.client;

import com.example.goldfinder.client.commands.IClientCommand;
import com.example.goldfinder.client.commands.SurroundingClient;
import com.example.utils.ClientCommandParser;
import com.example.utils.ConnectionMode;
import com.example.utils.Logger;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;

public class ClientBoi extends IClient {
    private boolean isPlaying = false;

    //ArrayDeque<IClientCommand> commandStack = new ArrayDeque<>();
    public ClientBoi(ConnectionMode mode) {
        super(mode);
        try {
            connect();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String updateClient(int xpos, int ypos) throws IOException {
        String msg = (mode == ConnectionMode.TCP ? receiveMessage(tcpSocket) : receiveMessage(udpSocket));
        if (!msg.isEmpty()) {
            return sendCommand(new SurroundingClient(), xpos + ":" + ypos);
        }
        return "";
    }

    public String sendCommand(IClientCommand command, String params) {
        if (command != null) {
            command.run(this, params);
            String resp;

            if (mode == ConnectionMode.TCP) {
                while (true) {
                    if (!(resp = receiveMessage(tcpSocket)).isEmpty()) return command.response(this, resp);
                }
            } else {
                while (true) {
                    if (!(resp = receiveMessage(udpSocket)).isEmpty()) return command.response(this, resp);
                }
            }
        }
        return "";
    }

    public void sendMessage(String msg) {
        if (mode == ConnectionMode.TCP) {
            sendMessage(tcpSocket, Wbuffer, msg);
            isPlaying = true;
        } else
            sendMessage(udpSocket, Wbuffer, msg);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean b) {
        isPlaying = b;
    }
}
