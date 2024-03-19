package com.example.goldfinder.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

import com.example.goldfinder.Player;
import com.example.utils.ConnectionMode;
import com.example.utils.Logger;

public abstract class IServer extends ICommon {
    private final List<String> backBuffer = new ArrayList<>();
    public ServerSocketChannel serverSocketChannel;
    protected Selector selector;
    Map<InetSocketAddress, Player> attachedPlayers = new HashMap<>();
    int playerID = 0;

    private Grid map;

    public IServer(int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        udpSocket = DatagramChannel.open();
        udpSocket.configureBlocking(false);
        udpSocket.bind(new InetSocketAddress("localhost", port));
        udpSocket.register(selector, SelectionKey.OP_READ);
    }

    public void startServer(Grid grid) throws IOException{
        this.map = grid;
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
        if(key.attachment() == null) {
            key.attach(new Player("Player" + playerID++, ConnectionMode.TCP, 0, 0));
            //envoyer la map ici
        }

        String msg = receiveTCPMessage((SocketChannel) key.channel());
        if (!msg.isEmpty()) {
            Logger.printDebug(((Player)key.attachment()).getName() +  " Received TCP message: " + msg);
            String currCom = msg.split(" ")[0];
            switch (currCom) {
                case "GAME_JOIN":
                    break;
            }
        }
    }

    private void handleUDPRead(SelectionKey key) throws IOException {
        InetSocketAddress senderAddress = (InetSocketAddress) ((DatagramChannel) key.channel()).receive(getrBuffer());
        if (!attachedPlayers.containsKey(senderAddress)) {
            attachedPlayers.put(senderAddress, new Player("Player" + playerID++, ConnectionMode.UDP, 0,0));
            //envoyer la map ici
        }
        key.attach(attachedPlayers.get(senderAddress));


        String msg = receiveUDPMessage(key);
        if (!msg.isEmpty()) {
            Logger.printDebug(((Player)key.attachment()).getName() +  " Received UDP message: " + msg);
        }
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

    private String receiveUDPMessage(SelectionKey key) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        try {
            ((DatagramChannel) key.channel()).receive(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer.flip();
        byte[] receivedBytes = new byte[buffer.remaining()];
        buffer.get(receivedBytes);
        return new String(receivedBytes);
    }


    private synchronized int sendUDPMessage(SelectionKey key, String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        return ((DatagramChannel) key.channel()).send(buffer, ((DatagramPacket) key.attachment()).getSocketAddress());
    }

    /*
     * public synchronized int sendMessage(SocketChannel client, ByteBuffer buffer,
     * String message) throws IOException {
     * if (super.sendMessage(client, buffer, message) == 0) {
     * addMessageToBackBuffer(message);
     * }
     * return 0;
     * }
     *
     * public synchronized int sendMessage(SelectionKey key, ByteBuffer buffer,
     * String message) throws IOException {
     * return sendMessage((SocketChannel) key.channel(), buffer, message);
     * }
     *
     * public void sendBackBuffer(SocketChannel client, ByteBuffer buffer) throws
     * IOException {
     * if (backBuffer.isEmpty()) {
     * return;
     * }
     *
     * System.out.println("Sending message w/ backbuffer");
     * for (String message : backBuffer) {
     * sendMessage(client, buffer, message);
     *
     * }
     * backBuffer.clear();
     * }
     *
     * public void addMessageToBackBuffer(String message) {
     * backBuffer.add(message);
     * }
     */
}