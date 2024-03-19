package com.example.goldfinder.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.List;

public abstract class IServer extends ICommon {
    protected final List<String> backBuffer = new ArrayList<>();
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
        udpSocket.bind(new InetSocketAddress("localhost", port));
        udpSocket.register(selector, SelectionKey.OP_READ);

    }

    protected String receiveUDPMessage(SelectionKey key) {
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
}
