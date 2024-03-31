package com.example.goldfinder.server;

import com.example.goldfinder.ICommon;
import com.example.utils.Logger;
import javafx.util.Pair;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.AbstractSelectableChannel;

public abstract class IServer extends ICommon {
    protected ServerSocketChannel serverSocketChannel;
    protected Selector selector;

    public IServer(int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        udpSocket = DatagramChannel.open();
        udpSocket.configureBlocking(false);
        udpSocket.bind(new InetSocketAddress(port));
        udpSocket.register(selector, SelectionKey.OP_READ);
    }

    public synchronized void sendMessage(SelectableChannel client, String message, SocketAddress address) {
        try {
            if (client instanceof SocketChannel) {
                sendTCPMessage((SocketChannel) client, message);
            } else if (client instanceof DatagramChannel) {
                sendUDPMessage((DatagramChannel) client, message, address);
            }
        } catch (IOException e) {
            Logger.printError("Could not send message : ");
            e.printStackTrace();
        }
    }


    protected void sendUDPMessage(AbstractSelectableChannel client, String message, SocketAddress address) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        ((DatagramChannel) client).send(buffer, address);
    }

    protected Pair<InetSocketAddress, String> receiveUDPMessage(SelectionKey key) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        try {
            InetSocketAddress pp = (InetSocketAddress) ((DatagramChannel) key.channel()).receive(buffer);
            buffer.flip();
            byte[] receivedBytes = new byte[buffer.remaining()];
            buffer.get(receivedBytes);
            return new Pair<>(pp, new String(receivedBytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Pair<>(new InetSocketAddress(1), "");
    }

    public abstract void startServer() throws IOException;

    protected void handleAccept(SelectionKey key) throws IOException{
        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        System.out.println("Connection accepted from " + client.getRemoteAddress());
    }

    protected abstract void handleTCPRead(SelectionKey key) throws IOException;

    protected abstract void handleUDPRead(SelectionKey key) throws IOException;
}
