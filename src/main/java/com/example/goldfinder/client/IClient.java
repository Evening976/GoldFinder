package com.example.goldfinder.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.example.goldfinder.server.ICommon;
import com.example.utils.Logger;

public abstract class IClient extends ICommon {
  ByteBuffer Wbuffer = ByteBuffer.allocate(128);
  Thread readerThread;

  public IClient() {
    Logger.printYellow("Trying to connect to server...");
    int attempts = 0;
    while (tcpSocket == null && attempts < 10) {
      try {
        tcpSocket = SocketChannel.open(new InetSocketAddress("127.0.0.1", 1234));
        tcpSocket.configureBlocking(false);
      } catch (Exception e) {
        attempts++;
        if (attempts == 10) {
          Logger.printError("Connection failed after 10 tries. Exiting...");
          clean();
        }
      }
    }
  }

  public void connect() throws IOException, InterruptedException {
    Logger.printSucess("Connected to server!");
    startReadThread();
  }

  protected abstract void handleClient() throws IOException, InterruptedException;


  protected void handleRead() throws IOException {
    String msg = receiveMessage(tcpSocket);
    if (!msg.isEmpty())
      System.out.println(Logger.getBlue("> : ") + msg);
  }

  protected void clean() {
    try {
      readerThread.interrupt();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void startReadThread() {
    readerThread = new Thread(() -> {
      while (true) {
        try {
          handleRead();
        } catch (IOException e) {
          System.out.println(Logger.getErrorLog("Connection with server has been closed"));
          clean();
          break;
        }
      }
    });
    readerThread.start();
  }
}

