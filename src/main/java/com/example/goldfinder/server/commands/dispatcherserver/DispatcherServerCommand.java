package com.example.goldfinder.server.commands.dispatcherserver;

import com.example.goldfinder.server.GameServer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.List;

public interface DispatcherServerCommand{
    String run(SelectableChannel client, SelectionKey k, List<GameServer> gameServers, InetSocketAddress addr, String[] params);
}
