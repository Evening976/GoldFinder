package com.example.goldfinder.server;

import com.example.utils.ConnectionMode;
import com.example.utils.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;

public abstract class ICommon {
    protected SocketChannel tcpSocket;
    protected DatagramChannel udpSocket;
    private final ByteBuffer rBuffer = ByteBuffer.allocate(1024);

    public synchronized String receiveMessage(SocketChannel client) {
        try {
            return receiveTCPMessage(client);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized String receiveMessage(DatagramChannel client) {
        return receiveUDPMessage(client);
    }

    public synchronized int sendMessage(SelectableChannel client, ByteBuffer buffer, String message) {
        try {
            if (client instanceof SocketChannel)
                return sendTCPMessage((SocketChannel) client, buffer, message);
            return sendUDPMessage((DatagramChannel) client, buffer, message);
        }
        catch (IOException e) {
            Logger.printError("Could not send message : ");
            e.printStackTrace();
        }
        return -1;
    }

    public synchronized ByteBuffer getrBuffer() {
        return rBuffer;
    }

    protected void closeSocket() {
        try {
            tcpSocket.close();
        } catch (IOException ignored) {
        }
    }

    protected synchronized String receiveTCPMessage(SocketChannel client) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        int bytesRead = client.read(buffer);

        if (bytesRead > 0) {
            buffer.flip();
            byte[] receivedBytes = new byte[bytesRead];
            buffer.get(receivedBytes);
            return new String(receivedBytes);
        }

        return "";
    }
    private synchronized int sendTCPMessage(SocketChannel client, ByteBuffer buffer, String message) throws IOException {
        ByteBuffer z = ByteBuffer.allocate(128);
        z.clear();
        z.put(message.getBytes());
        z.flip();
        return client.write(z);
    }

    private synchronized int sendUDPMessage(DatagramChannel client, ByteBuffer buffer, String message) throws IOException {
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        return client.send(buffer, new InetSocketAddress("127.0.0.1", 1234));
    }


    private String receiveUDPMessage(DatagramChannel client) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        try {
            client.receive(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer.flip();
        byte[] receivedBytes = new byte[buffer.remaining()];
        buffer.get(receivedBytes);
        return new String(receivedBytes);
    }
}
