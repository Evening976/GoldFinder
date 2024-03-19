package com.example.goldfinder.server;

import com.example.goldfinder.Player;
import com.example.utils.ConnectionMode;
import com.example.utils.GameMap;
import com.example.utils.Logger;
import com.example.utils.gdGame;
import javafx.util.Pair;

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
        //TODO:Déplacer les commandes dans une classe dédiée
        String currCom = msg.split(" ")[0];
        Player player = (Player) key.attachment();
        switch (currCom) {
            case "GAME_JOIN":
                String playerName = msg.split(" ")[1];
                player.setName(playerName);
                Pair<Short, gdGame> p = games.getAvailable();
                player.attachToGame(p.getKey());
                p.getValue().addPlayer(player);
                System.out.println("Player " + playerName + " has joined game : " + player.getGameID());
                break;
            case "SURROUNDING":
                //send map
                break;
            case "dir":
                String direction = msg.split(" ")[1];
                switch (direction) {
                    case "UP":
                        break;
                    case "DOWN":
                        break;
                    case "LEFT":
                        break;
                    case "RIGHT":
                        break;
                }
                break;
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
}