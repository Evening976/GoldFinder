package com.example.goldfinder.server;

import com.example.goldfinder.server.commands.gameserver.GameServerCommand;
import com.example.utils.ConnectionMode;
import com.example.utils.Logger;
import com.example.goldfinder.server.commands.gameserver.GameServerCommandParser;
import com.example.utils.games.AbstractGame;
import com.example.utils.games.GameMap;
import com.example.utils.players.AbstractPlayer;
import javafx.util.Pair;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameServer extends IServer {
    private final Map<InetSocketAddress, AbstractPlayer> attachedPlayers = new HashMap<>();
    private final GameMap games;
    private static final int MAX_PLAYERS = 4;

    public GameServer(int port, int maxGames) throws IOException {
        super(port);
        games = new GameMap(MAX_PLAYERS, maxGames);
    }

    public void startServer() throws IOException {
        while (true) {
            if (selector.select() == 0) {
                continue;
            }
            Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
            while (selectedKeys.hasNext()) {
                SelectionKey key = selectedKeys.next();
                try {
                    if (key.isAcceptable()) {
                        handleAccept(key);
                    } else if (key.isReadable()) {
                        if (key.channel() instanceof SocketChannel) handleTCPRead(key);
                        else handleUDPRead(key);
                    }
                    selectedKeys.remove();
                } catch (IOException e) {
                    AbstractPlayer p = (AbstractPlayer) key.attachment();
                    if (p.getGameID() != null) games.getByID(p.getGameID()).removePlayer(p);
                    key.channel().close();
                    key.cancel();
                    Logger.printYellow("Connection with client has been closed : " + e.getMessage());
                }
            }
        }
    }

    public GameMap getGames() {
        return games;
    }

    public InetSocketAddress getAddr(ConnectionMode mode) {
        try {
            return mode == ConnectionMode.TCP ? new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), serverSocketChannel.socket().getLocalPort())
                    : new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), udpSocket.socket().getLocalPort());
        } catch (IOException e) {
            System.out.println("Could not retrieve gameserver's address");
        }
        return null;
    }


    protected void handleTCPRead(SelectionKey key) throws IOException {
        InetSocketAddress senderAddress = (InetSocketAddress) ((SocketChannel) key.channel()).getRemoteAddress();
        String msg = receiveTCPMessage((SocketChannel) key.channel());
        if (!msg.isEmpty()) {
            SelectionKey k = handleCommands(key, msg, senderAddress);
            attachedPlayers.put(senderAddress, (AbstractPlayer) k.attachment());
        }
    }

    protected void handleUDPRead(SelectionKey key) throws IOException {
        Pair<InetSocketAddress, String> messageandIp = receiveUDPMessage(key);
        String msg = messageandIp.getValue();
        InetSocketAddress senderAddress = messageandIp.getKey();

        if (attachedPlayers.containsKey(senderAddress)) {
            key.attach(attachedPlayers.get(senderAddress));
        }

        if (!msg.isEmpty()) {
            SelectionKey k = handleCommands(key, msg, senderAddress);
            attachedPlayers.put(senderAddress, (AbstractPlayer) k.attachment());
        }
    }


    private SelectionKey handleCommands(SelectionKey key, String msg, InetSocketAddress... senderAddress) {
        AbstractPlayer player = (AbstractPlayer) key.attachment();
        AbstractGame g = player == null ? null : games.getByID(player.getGameID());

        GameServerCommand currentCommand = GameServerCommandParser.parseCommand(msg);
        if (currentCommand != null) {
            String response = currentCommand.run(key.channel(), this, player, g, senderAddress[0], msg.split(" "));
            player = currentCommand.getPlayer();
            games.setGame(player.getGameID(), currentCommand.getGame());
            sendMessage(key.channel(), response, player.getAddress());
            System.out.println("Game server data : " + player + " game : " + games.getByID(player.getGameID()));
        }
        key.attach(player);
        return key;
    }

    //rajouter une liste de client qui attendent le leaderboard et faire une methode que appelle le dipsatcher pour envoyer le leaderboard?
}