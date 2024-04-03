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
        int count = 0;
        int nGames = 0;
        for(GameServer gameServer : gameServers){
            count++;
            nGames += gameServer.getGames().getRunningGames();
            if(gameServer.getGames().getRunningGames() < GAME_COUNT){
                System.out.println("Server fully busy : " + count + "/" + gameServers.size());
                System.out.println("Games running : " + nGames);
                return "REDIRECT " + gameServer.getAddr(ConnectionMode.valueOf((String)k.attachment())).getAddress().getHostAddress() +
                        ":" + gameServer.getAddr(ConnectionMode.valueOf((String)k.attachment())).getPort();
            }
        }
        return "GAME_END";
    }
}
