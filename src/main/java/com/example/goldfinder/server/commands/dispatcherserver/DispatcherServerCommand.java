package com.example.goldfinder.server.commands.dispatcherserver;

import com.example.goldfinder.server.GameServer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.util.List;

public interface DispatcherServerCommand{
    String run(SelectableChannel client, List<GameServer> gameServers, InetSocketAddress addr, String[] params);
}
