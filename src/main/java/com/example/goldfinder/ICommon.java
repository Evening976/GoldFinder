package com.example.goldfinder;

import com.example.utils.ConnectionMode;
import com.example.utils.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public abstract class ICommon {
    protected SocketChannel tcpSocket;
    protected DatagramChannel udpSocket;

    public synchronized String receiveMessage(ConnectionMode mode){
        try {
            if(mode == ConnectionMode.TCP) {
                return receiveTCPMessage(tcpSocket);
            } else {
                return receiveUDPMessage(udpSocket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void sendMessage(ConnectionMode mode, String message) {
        try {
            if(mode == ConnectionMode.TCP) {
                sendTCPMessage(tcpSocket, message);
            } else {
                sendUDPMessage(udpSocket, message);
            }
        }
        catch (IOException e) {
            Logger.printError("Could not send message : ");
            e.printStackTrace();
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
    protected synchronized void sendTCPMessage(SocketChannel client, String message) throws IOException {
        ByteBuffer z = ByteBuffer.allocate(message.getBytes().length);
        z.clear();
        z.put(message.getBytes());
        z.flip();
        client.write(z);
    }

    synchronized void sendUDPMessage(DatagramChannel client, String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(message.getBytes().length);
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();

        ByteBuffer p = ByteBuffer.allocate(128);
        p.clear();
        p.put(message.getBytes());
        p.flip();
        System.out.println("Sending UDP message : " + StandardCharsets.UTF_8.decode(p).toString());
        client.send(buffer, client.getRemoteAddress());
    }

    String receiveUDPMessage(DatagramChannel client) {
        ByteBuffer buffer = ByteBuffer.allocate(128);
        buffer.clear();
        try {
            client.receive(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer.flip();
        String receivedBytes = StandardCharsets.UTF_8.decode(buffer).toString();
        return new String(receivedBytes);
    }
}
