package com.example.goldfinder.server.commands.dispatcherserver;

import com.example.goldfinder.server.GameServer;
import com.example.goldfinder.server.ScoreManager;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class D_Leaderboard implements DispatcherServerCommand {
    @Override
    public String run(SelectableChannel client, SelectionKey k, List<GameServer> gameServers, InetSocketAddress addr, String[] params) {
        TreeMap<Integer, ArrayList<String>> biggesMapEver = new TreeMap<>();
        for(GameServer gameServer : gameServers){
            biggesMapEver.putAll(gameServer.getScores());
        }
        return ScoreManager.getLeaderboardsText(biggesMapEver, Integer.parseInt(params[1]));
    }
}
