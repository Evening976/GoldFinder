package com.example.goldfinder.server;

import com.example.goldfinder.server.commands.IServerCommand;
import com.example.utils.*;
import com.example.utils.CommandParsers.ServerCommandParser;
import com.example.utils.Games.GameMap;
import com.example.utils.Games.gdGame;
import com.example.utils.Players.AbstractPlayer;
import com.example.utils.Players.CopsPlayer;
import com.example.utils.Players.GFPlayer;
import javafx.util.Pair;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class GameServer extends IServer {
    private final Map<InetSocketAddress, AbstractPlayer> attachedPlayers = new HashMap<>();
    private final GameMap games;
    int playerID = 0;

    String GAMEMODE = "GOLD_FINDER";
    private final int MAX_PLAYERS = 4;

    public GameServer(int port) throws IOException {
        super(port);
        games = new GameMap(MAX_PLAYERS, 10);
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
                    } else if (key.isWritable()) {
                        handleBackBuffer((SocketChannel) key.channel(), getrBuffer());
                    }
                    selectedKeys.remove();
                } catch (IOException e) {
                    GFPlayer p = (GFPlayer) key.attachment();
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
            if(Objects.equals(GAMEMODE, "GOLD_FINDER"))
                key.attach(new GFPlayer(key.channel(), "GFPlayer" + playerID++, ConnectionMode.TCP, 0, 0));
            else
                key.attach(new CopsPlayer(key.channel(), "CopsPlayer" + playerID++, ConnectionMode.TCP, 0, 0));
        }

        String msg = receiveTCPMessage((SocketChannel) key.channel());
        if (!msg.isEmpty()) {
            Logger.printDebug(((AbstractPlayer) key.attachment()).getName() + "("+((AbstractPlayer) key.attachment()).getPlayerID()  + ") Received TCP message: " + msg);
            handleCommand(key, msg);
        }
    }

    private void handleUDPRead(SelectionKey key) throws IOException {
        Pair<InetSocketAddress, String> messageandIp = receiveUDPMessage(key);
        String msg = messageandIp.getValue();
        InetSocketAddress senderAddress = messageandIp.getKey();
        System.out.println("Received UDP message from " + senderAddress);

        if (!attachedPlayers.containsKey(senderAddress)) {
            if(Objects.equals(GAMEMODE, "GOLD_FINDER"))
                attachedPlayers.put(senderAddress, new GFPlayer(key.channel(), "Player" + playerID++, ConnectionMode.UDP, 0, 0));
            else
                attachedPlayers.put(senderAddress, new CopsPlayer(key.channel(), "Player" + playerID++, ConnectionMode.UDP, 0, 0));
        }
        key.attach(attachedPlayers.get(senderAddress));

        System.out.println("Received UDP message: " + msg);
        if (!msg.isEmpty()) {
            Logger.printDebug(((AbstractPlayer) key.attachment()).getName() + " Received UDP message: " + msg);
            SelectionKey k = handleCommand(key, msg);
            attachedPlayers.put(senderAddress, (AbstractPlayer) k.attachment());
        }
    }

    private Pair<InetSocketAddress, String> receiveUDPMessage(SelectionKey key) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        try {
            InetSocketAddress pp = (InetSocketAddress)((DatagramChannel) key.channel()).receive(buffer);
        buffer.flip();
        byte[] receivedBytes = new byte[buffer.remaining()];
        buffer.get(receivedBytes);
        return new Pair<>(pp, new String(receivedBytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Pair<>(new InetSocketAddress(1), "");
    }

    private synchronized int sendUDPMessage(SelectionKey key, String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        return ((DatagramChannel) key.channel()).send(buffer, ((DatagramPacket) key.attachment()).getSocketAddress());
    }

    private SelectionKey handleCommand(SelectionKey key, String msg) {
        AbstractPlayer player = (AbstractPlayer) key.attachment();
        gdGame g = null;

        if (player.getGameID() != null)
            g = games.getByID(player.getGameID());

        IServerCommand currentCommand = ServerCommandParser.parseCommand(msg);
        if (currentCommand != null) {
            if (g == null)
                sendMessage(key.channel(), getrBuffer(), currentCommand.run(key.channel(), this, player, null, msg.split(" ")));
            else
                sendMessage(key.channel(), getrBuffer(), currentCommand.run(key.channel(), this, player, games.getByID(player.getGameID()), msg.split(" ")));

            player = currentCommand.getPlayer();
            games.setGame(player.getGameID(), currentCommand.getGame());

            System.out.println("Game server data : " + player + " game : " + games.getByID(player.getGameID()));
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

    public GameMap getGames() {
        return games;
    }
}