package com.example.goldfinder.server;

import com.example.utils.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
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

    public synchronized int sendMessage(SelectableChannel client, ByteBuffer buffer, String message) {
        try {
            if (client instanceof SocketChannel){return sendTCPMessage((SocketChannel) client, buffer, message);}
            else if(client instanceof DatagramChannel) { return sendUDPMessage((DatagramChannel) client, buffer, message);}
        }
        catch (IOException e) {
            Logger.printError("Could not send message : ");
            e.printStackTrace();
        }
        return -1;
    }
}
