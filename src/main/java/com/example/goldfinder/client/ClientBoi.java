package com.example.goldfinder.client;

import com.example.goldfinder.client.commands.IClientCommand;
import com.example.goldfinder.client.commands.SurroundingClient;
import com.example.utils.commandParsers.ClientCommandParser;

public class ClientBoi extends IClient {
    private boolean isConnected = false;
    private boolean isPlaying = false;

    public ClientBoi() {
        super();
    }

    public IClientCommand updateClient(){
        if(!isConnected) return null;
        String command = receiveMessage(mode);
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
