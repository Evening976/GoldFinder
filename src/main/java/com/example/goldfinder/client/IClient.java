package com.example.goldfinder.client;

import com.example.goldfinder.server.ICommon;
import com.example.utils.ConnectionMode;
import com.example.utils.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

import static java.lang.System.exit;
import static java.lang.System.out;

public abstract class IClient extends ICommon {
  ByteBuffer Wbuffer = ByteBuffer.allocate(128);
  ConnectionMode mode;
  Thread readerThread;

  public IClient(ConnectionMode mode) {
    this.mode = mode;
    Logger.printYellow("Trying to connect to server...");

    try{
      if(mode == ConnectionMode.TCP) startTCPConnection();
      else startUDPConnection();
    } catch (IOException e) {
      Logger.printError("Connection failed. Exiting...");
      clean();
    }
  }

  private void startTCPConnection(){
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

    private void startUDPConnection() throws IOException {
      udpSocket = DatagramChannel.open();
      udpSocket.configureBlocking(false);
      udpSocket.connect(new InetSocketAddress("127.0.0.1", 1234));
    }

  public void connect() throws IOException, InterruptedException {
    if(tcpSocket != null || udpSocket != null) Logger.printSucess("Connected to server!");
    //while(true) handleClient();
  }

  public abstract String updateSurrounding(int xpos, int ypos) throws IOException, InterruptedException;


  protected void handleRead() throws IOException {
    String msg;
    msg = receiveMessage(mode);
    if (!msg.isEmpty())
      out.println(Logger.getBlue("> : ") + msg);
  }

  protected void clean() {
    try {
      if(readerThread!=null) readerThread.interrupt();
      if(tcpSocket !=null) tcpSocket.close();
      exit(0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

