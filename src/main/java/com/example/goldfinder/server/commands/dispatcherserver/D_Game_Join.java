package com.example.goldfinder.server.commands.dispatcherserver;

import com.example.goldfinder.server.GameServer;
import com.example.utils.ConnectionMode;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.List;

import static com.example.goldfinder.server.DispatcherServer.GAME_COUNT;

public class D_Game_Join implements DispatcherServerCommand {
    @Override
    public String run(SelectableChannel client, SelectionKey k, List<GameServer> gameServers, InetSocketAddress addr, String[] params) {
        for(GameServer gameServer : gameServers){
            if(gameServer.getGames().getRunningGames() < GAME_COUNT){
                return "REDIRECT " + gameServer.getAddr(ConnectionMode.valueOf((String)k.attachment())).getAddress().getHostAddress() +
                        ":" + gameServer.getAddr(ConnectionMode.valueOf((String)k.attachment())).getPort();
            }
        }
        return null;
    }
}
