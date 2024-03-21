package com.example.goldfinder.server;

import com.example.utils.Player;
import com.example.utils.*;
import com.example.goldfinder.server.commands.IServerCommand;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameServer extends IServer {
    private final Map<InetSocketAddress, Player> attachedPlayers = new HashMap<>();
    private final GameMap games;
    int playerID = 0;

    public GameServer(int port) throws IOException {
        super(port);
        games = new GameMap(4, 10);
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
                        if (key.channel() instanceof SocketChannel)
                            handleTCPRead(key);
                        else
                            handleUDPRead(key);
                    } else if (key.isWritable()) {
                        handleBackBuffer((SocketChannel) key.channel(), getrBuffer());
                    }
                    selectedKeys.remove();
                } catch (IOException e) {
                    Player p = (Player) key.attachment();
                    if (p.getGameID() != null) games.getByID(p.getGameID()).removePlayer(p);
                    key.channel().close();
                    key.cancel();
                    Logger.printYellow("Connection with client has been closed : " + e.getMessage());
                }
            }
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }


    private void handleTCPRead(SelectionKey key) throws IOException {
        if (key.attachment() == null) {
            key.attach(new Player("Player" + playerID++, ConnectionMode.TCP, 0, 0));
        }

        String msg = receiveTCPMessage((SocketChannel) key.channel());
        if (!msg.isEmpty()) {
            Logger.printDebug(((Player) key.attachment()).getName() + " Received TCP message: " + msg);
            handleCommand(key, msg);
        }
    }

    private void handleUDPRead(SelectionKey key) throws IOException {
        InetSocketAddress senderAddress = (InetSocketAddress) ((DatagramChannel) key.channel()).receive(getrBuffer());
        if (!attachedPlayers.containsKey(senderAddress)) {
            attachedPlayers.put(senderAddress, new Player("Player" + playerID++, ConnectionMode.UDP, 0, 0));
        }
        key.attach(attachedPlayers.get(senderAddress));


        String msg = receiveUDPMessage(key);
        if (!msg.isEmpty()) {
            Logger.printDebug(((Player) key.attachment()).getName() + " Received UDP message: " + msg);
            SelectionKey k = handleCommand(key, msg);
            attachedPlayers.put(senderAddress, (Player) k.attachment());
        }
    }

    private SelectionKey handleCommand(SelectionKey key, String msg) {
        Player player = (Player) key.attachment();
        gdGame g = null;

        if(player.getGameID() != null)
            g = games.getByID(player.getGameID());

        IServerCommand currentCommand = ServerCommandParser.parseCommand(msg);
        if (currentCommand != null) {
            if(g == null)
                System.out.println(currentCommand.run(this, player, null, msg.split(" ")));
            else System.out.println(currentCommand.run(this, player, games.getByID(player.getGameID()), msg.split(" ")));

            player = currentCommand.getPlayer();
            games.setGame(player.getGameID(), currentCommand.getGame());
        }
        key.attach(player);
        return key;
    }

    private void handleBackBuffer(SocketChannel client, ByteBuffer buffer) throws IOException {
        if (backBuffer.isEmpty()) {
            return;
        }

        Logger.printDebug("Sending message w/ backbuffer");
        for (String message : backBuffer) {
            sendMessage(client, buffer, message);
        }
        backBuffer.clear();
    }

    public GameMap getGames(){
        return games;
    }
}