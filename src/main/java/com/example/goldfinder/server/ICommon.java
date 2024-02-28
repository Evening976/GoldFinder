package com.example.goldfinder.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.example.goldfinder.Player;
import com.example.utils.ConnectionMode;

public abstract class ICommon {
  protected SocketChannel tcpSocket;
  protected DatagramChannel udpSocket;
  private ByteBuffer rBuffer = ByteBuffer.allocate(1024);

  public synchronized String receiveMessage(SelectionKey key) throws IOException {
    Player player = (Player) key.attachment();
    if (player.getConnectionMode() == ConnectionMode.TCP)
      return receiveTCPMessage((SocketChannel) key.channel());
    else
      return receiveUDPMessage(key);
  }

  public synchronized String receiveMessage(SocketChannel client) throws IOException {
    return receiveTCPMessage(client);
  }

  public synchronized int sendMessage(SelectionKey key, String message) throws IOException {
    Player player = (Player) key.attachment();
    if (player.getConnectionMode() == ConnectionMode.TCP)
      return sendTCPMessage((SocketChannel) key.channel(), getrBuffer(), message);
    else
      return sendUDPMessage(key, message);
  }

  public synchronized int sendMessage(SocketChannel client, ByteBuffer buffer, String message) throws IOException {
    return sendTCPMessage(client, buffer, message);
  }

  public synchronized ByteBuffer getrBuffer() {
    return rBuffer;
  }

  protected void closeSocket() {
    try {
      tcpSocket.close();
    } catch (IOException e) {
    }
  }

  private synchronized int sendTCPMessage(SocketChannel client, ByteBuffer buffer, String message) throws IOException {
    ByteBuffer z = ByteBuffer.allocate(128);
    z.clear();
    z.put(message.getBytes());
    z.flip();
    return client.write(z);
  }

  private synchronized int sendUDPMessage(SelectionKey key, String message) throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    buffer.clear();
    buffer.put(message.getBytes());
    buffer.flip();
    return ((DatagramChannel) key.channel()).send(buffer, ((DatagramPacket) key.attachment()).getSocketAddress());
  }

  private synchronized String receiveTCPMessage(SocketChannel client) throws IOException {
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
}
