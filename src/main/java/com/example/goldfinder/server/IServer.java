package com.example.goldfinder.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.goldfinder.Player;
import com.example.utils.Logger;

public abstract class IServer extends ICommon {
  private List<String> backBuffer = new ArrayList<String>();
  public ServerSocketChannel serverSocketChannel;
  protected Selector selector;

  private Grid map;

  public IServer(int port) throws IOException {
    selector = Selector.open();
    serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.socket().bind(new InetSocketAddress(port));
    serverSocketChannel.configureBlocking(false);
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
  }

  public void startServer(Grid grid){
    this.map = grid;
    while(true){
          try {
        if (selector.select() == 0){
          continue;}
        Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
        while (selectedKeys.hasNext()) {
          SelectionKey key = selectedKeys.next();
          if (key.isAcceptable()) {
            handleAccept(key); //que tcp ?
          } else if (key.isReadable()) {
            if(key.channel() instanceof SocketChannel)
              handleTCPRead(key);
            else
              handleUDPRead(key);
          } else if (key.isWritable()) {
            handleBackBuffer((SocketChannel) key.channel(), getrBuffer());
          }
          selectedKeys.remove();
        }
      } catch (IOException e) {
        Logger.printYellow("Connection with client has been closed");
      }
    }
  }

  private void handleAccept(SelectionKey key) throws IOException {
    SocketChannel client = serverSocketChannel.accept();
    client.configureBlocking(false);
    client.register(selector, SelectionKey.OP_READ);

    Logger.printDebug("Connection Accepted: " + client.getRemoteAddress());
  }


  private void handleTCPRead(SelectionKey key){
    try {
      String msg = receiveMessage(key);
      if (!msg.isEmpty()) {
        String currCom = msg.split(" ")[0];
        switch(currCom){
          case "GAME_JOIN":
            break;
        }
      }
    } catch (IOException e) {
      Logger.printYellow("Connection with client has been closed");
    }
  }

  private void handleUDPRead(SelectionKey key){
    try{
      String msg = "";//= receiveUDPMessage(key);
      if(!msg.isEmpty()){
      }
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