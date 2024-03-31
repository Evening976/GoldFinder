package com.example.goldfinder.server.commands.dispatcherserver;

import com.example.goldfinder.server.GameServer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.util.List;

public class D_Leaderboard implements DispatcherServerCommand {
    @Override
    public String run(SelectableChannel client, List<GameServer> gameServers, InetSocketAddress addr, String[] params) {
        return null;
    }
}
