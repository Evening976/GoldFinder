package com.example.goldfinder.server.commands.dispatcherserver;

import com.example.goldfinder.server.GameServer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.util.List;

import static com.example.goldfinder.server.DispatcherServer.GAME_COUNT;

public class D_Game_Join implements DispatcherServerCommand {
    @Override
    public String run(SelectableChannel client, List<GameServer> gameServers, InetSocketAddress addr, String[] params) {
        for(GameServer gameServer : gameServers){
            if(gameServer.getGames().getRunningGames() < GAME_COUNT){
                return "REDIRECT " + gameServer.getAddr().getAddress().getHostAddress() + ":" + gameServer.getAddr().getPort();
            }
        }
        return null;
    }
}
