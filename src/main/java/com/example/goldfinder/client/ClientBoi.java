package com.example.goldfinder.client;

import com.example.goldfinder.client.commands.IClientCommand;
import com.example.goldfinder.client.commands.SurroundingClient;
import com.example.utils.ClientCommandParser;
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

    public IClientCommand updateClient(){
        String command = receiveMessage(mode);
        return ClientCommandParser.parseCommand(command);
    }

    @Override
    public String updateSurrounding(int xpos, int ypos) {
        if (isPlaying) return sendCommand(new SurroundingClient(), "");
        return "";
    }

    public String sendCommand(IClientCommand command, String params) {
        command.run(this, params);
        String resp = "";
        while (true) {
            if (!(resp = receiveMessage(mode)).isEmpty()) {
                return command.response(this, resp);
            }
        }
    }

    public void sendMessage(String msg) {
        sendMessage(mode, Wbuffer, msg);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean b) {
        isPlaying = b;
    }
}
