package com.example.goldfinder.client.commands;

import com.example.goldfinder.client.ClientBoi;

public class Client_Leaderboard implements IClientCommand {
    @Override
    public String getName() {
        return "LEADER";
    }

    @Override
    public String run(ClientBoi boi, String params) {
        if (!boi.isPlaying() && !boi.isConnected()) return "You are not connected to a server!";
        boi.sendMessage("LEADER " + params);
        return "";
    }

    @Override
    public String response(ClientBoi boi, String msg) {
        StringBuilder sb = new StringBuilder();
        String[] lines = msg.split("\n");
        for (String line : lines) {
            if(line.contains("END")) break;
            String[] split = line.split(":");
            sb.append(split[1]).append(" ").append(split[2]).append("\n");
        }
        return sb.toString();
    }
}
